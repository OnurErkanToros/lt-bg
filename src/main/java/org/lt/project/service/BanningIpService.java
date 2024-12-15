package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.exception.customExceptions.BadRequestException;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.BanningIp;
import org.lt.project.repository.BanningIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanningIpService {
    private final BanningIpRepository banningIpRepository;

    public Page<BanningIp> getUntransferedIpList(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BanningIp> bannedIpList = banningIpRepository.findAllByStatus(BanningIp.BanningIpStatus.NOT_TRANSFERRED, pageable);
        if(bannedIpList.isEmpty()){
            throw new ResourceNotFoundException("Transfer edilmemiş ip yok.");
        }
        return bannedIpList;
    }

    public List<BanningIp> getBannedIpList() {
        return banningIpRepository.findAllByStatus(BanningIp.BanningIpStatus.TRANSFERRED);
    }

    public List<BanningIp> addAllIp(List<BanningIp> banningIps) {
        List<String> ipList = banningIps.stream().map(BanningIp::getIp).toList();
        List<BanningIp> existingIpAddress = banningIpRepository.findByIpIn(ipList);
        List<BanningIp> filteredIpAddress = banningIps.stream()
                    .filter(ip-> existingIpAddress
                            .stream()
                            .noneMatch(existingIp ->existingIp.getIp().equals(ip.getIp())))
                    .toList();
        return banningIpRepository.saveAll(filteredIpAddress);
    }

    public BanningIp addBannedIp(BanningIp banningIp) {
        Optional<BanningIp> existingBannedIp = banningIpRepository.findByIp(banningIp.getIp());
        if (existingBannedIp.isPresent()) {
            throw new BadRequestException("Bu ip zaten var.");
        }
        return banningIpRepository.save(banningIp);
    }

    public Long getUnTransferredCount() {
        return banningIpRepository.countAllByStatus(BanningIp.BanningIpStatus.NOT_TRANSFERRED);
    }
}
