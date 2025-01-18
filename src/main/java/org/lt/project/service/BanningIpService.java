package org.lt.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.lt.project.dto.BanIpRequest;
import org.lt.project.dto.resultDto.ErrorResult;
import org.lt.project.dto.resultDto.Result;
import org.lt.project.dto.resultDto.SuccessResult;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.BanningIpRepository;
import org.lt.project.specification.BanningIpSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanningIpService {
    private final BanningIpRepository banningIpRepository;
  private final FileService fileService;
  private final SuspectIpService suspectIpService;

    public Page<BanningIp> getBannedAllIpPageable(String ip, BanningIp.BanningIpType ipType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Specification<BanningIp> spec = Specification.where(BanningIpSpecification.hasIp(ip)).and(BanningIpSpecification.hasIpType(ipType));
        return banningIpRepository.findAll(spec,pageable);
    }

    public List<BanningIp> getBannedAllIp(){
        return banningIpRepository.findAll();
    }

    public void addAllIp(List<BanningIp> banningIps) {
        List<String> ipList = banningIps.stream().map(BanningIp::getIp).toList();
        List<BanningIp> existingIpAddress = banningIpRepository.findByIpIn(ipList);
        List<BanningIp> filteredIpAddress = banningIps.stream()
                .filter(ip -> existingIpAddress
                        .stream()
                        .noneMatch(existingIp -> existingIp.getIp().equals(ip.getIp())))
                .toList();
        banningIpRepository.saveAll(filteredIpAddress);
    }

    public void addBannedIp(BanningIp banningIp) {
        Optional<BanningIp> existingBannedIp = banningIpRepository.findFirstByIp(banningIp.getIp());
        if (existingBannedIp.isPresent()) {
            throw new BadRequestException("Bu ip zaten engellenmiş.");
        }
        banningIpRepository.save(banningIp);
    }

    public void deleteIpAddresses(List<String> ipList) {
        List<BanningIp> banningIpList = banningIpRepository.findByIpIn(ipList);
        if (!banningIpList.isEmpty()) {
            banningIpRepository.deleteAll(banningIpList);
        }
    }

    public boolean isHaveAnyIP(String ip) {
        Optional<BanningIp> optionalBanningIp = banningIpRepository.findFirstByIp(ip);
        return optionalBanningIp.isPresent();
    }

  public List<Result> banThisIpList(List<BanIpRequest> banIpRequestList) {
    List<Result> resultList = new ArrayList<>();
    if (banIpRequestList.isEmpty()) {
      {
        throw new BadRequestException("Ip listesi boş olamaz.");
      }
    }
    banIpRequestList.forEach(
        banIpRequest -> {
          var isHaveBanningIpList = isHaveAnyIP(banIpRequest.ip());
          var isHaveFileIpList = fileService.isHaveAnyIp(banIpRequest.ip());

          if (isHaveBanningIpList && isHaveFileIpList) {
            if (banIpRequest.ipType() == BanningIp.BanningIpType.LISTENER) {
              suspectIpService.changeStatus(SuspectIP.IpStatus.BANNED, banIpRequest.ip());
            }
            resultList.add(
                new ErrorResult(
                    String.format("%s zaten dosyada var, yani banlanmış.", banIpRequest.ip())));
          } else if (isHaveBanningIpList) {
            fileService.addIPAddress(banIpRequest.ip());
            resultList.add(
                new SuccessResult(
                    String.format(
                        "%s veritabanında vardı ama dosyada yoktu. Dosyaya eklendi.",
                        banIpRequest.ip())));
          } else {
              if (banIpRequest.ipType() == BanningIp.BanningIpType.LISTENER) {
                  suspectIpService.changeStatus(SuspectIP.IpStatus.BANNED, banIpRequest.ip());
              }
              fileService.addIPAddress(banIpRequest.ip());
              addBannedIp(
                BanningIp.builder()
                    .ip(banIpRequest.ip())
                    .ipType(banIpRequest.ipType())
                    .createdBy(UserService.getAuthenticatedUser())
                    .createdAt(new Date())
                    .build());
            resultList.add(
                new SuccessResult(
                    String.format("%s dosyaya ve veritabanına eklendi.", banIpRequest.ip())));
          }
        });
    return resultList;
  }

  public List<Result> unbanThisIpList(List<BanIpRequest> banIpRequestList) {
    List<Result> resultList = new ArrayList<>();
    if (banIpRequestList.isEmpty()) {
      throw new BadRequestException("Ip listesi boş olamaz.");
    }
    List<String> ipList = banIpRequestList.stream().map(BanIpRequest::ip).toList();
    deleteIpAddresses(ipList);
    banIpRequestList.forEach(
        banIpRequest -> {

          if (fileService.isHaveAnyIp(banIpRequest.ip())) {
            fileService.removeIPAddress(banIpRequest.ip());
          }
          if (banIpRequest.ipType() == BanningIp.BanningIpType.LISTENER) {
            suspectIpService.changeStatus(SuspectIP.IpStatus.CANCEL_BAN, banIpRequest.ip());
          }
          resultList.add(
              new SuccessResult(
                  String.format("%s dosyadan ve veritabanından silindi.", banIpRequest.ip())));
        });
    return resultList;
  }
}
