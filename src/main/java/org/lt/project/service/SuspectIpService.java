package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.lt.project.specification.SuspectIpSpecification;
import org.lt.project.util.convertor.SuspectIpConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuspectIpService {
    private final SuspectIpRepository suspectIpRepository;
    private final BanningIpService banningIpService;
    private final FileService fileService;


    public Page<SuspectIpResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return suspectIpRepository.findAll(pageable).map(SuspectIpConverter::convert);
    }

    public Page<SuspectIpResponseDto> getAllFiltered(int page, int size, SuspectIP.IpStatus ipStatus, String host, String ip) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Specification<SuspectIP> spec = Specification.where(SuspectIpSpecification.hasIp(ip))
                .and(SuspectIpSpecification.hasStatus(ipStatus))
                .and(SuspectIpSpecification.hasHost(host));
        return suspectIpRepository.findAll(spec, pageable).map(SuspectIpConverter::convert);
    }

    public SuspectIP save(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        return suspectIpRepository.save(suspectIP);
    }

    public Boolean setBanSuspectIpList(List<BanRequestDto> banRequestDtoList) {
        List<String> banList = banRequestDtoList.stream().map(BanRequestDto::ip).toList();
        String user = UserService.getAuthenticatedUser();
        List<SuspectIP> suspectIPListNewAndCancelBan = suspectIpRepository
                .findAllByStatusInAndIpAddressIn(List.of(SuspectIP.IpStatus.NEW,
                        SuspectIP.IpStatus.CANCEL_BAN), banList);
        if (!suspectIPListNewAndCancelBan.isEmpty()) {
            suspectIPListNewAndCancelBan.forEach(suspectIP -> {
                suspectIP.setStatus(SuspectIP.IpStatus.BANNED);
                suspectIP.setStatusAt(new Date());
                suspectIP.setStatusBy(user);
            });
            suspectIpRepository.saveAll(suspectIPListNewAndCancelBan);
        }

        List<BanningIp> ipListForBanningIp = new ArrayList<>();
        List<String> ipListForFile = new ArrayList<>();
        for (String ip : banList) {
            if (!banningIpService.isHaveAnyIP(ip)) {
                ipListForBanningIp.add(
                        BanningIp.builder()
                                .ip(ip)
                                .ipType(BanningIp.BanningIpType.LISTENER)
                                .createdAt(new Date())
                                .createdBy(user)
                                .build());
            }
            if (!fileService.isHaveAnyIp(ip)) {
                ipListForFile.add(ip);
            }
        }
        if (!ipListForFile.isEmpty()) {
            fileService.addIPAddresses(ipListForBanningIp.stream().map(BanningIp::getIp).toList());
        }
        if (!ipListForBanningIp.isEmpty()) {
            banningIpService.addAllIp(ipListForBanningIp);
        }
        return true;
    }

    public Boolean setUnbanSuspectIpList(List<BanRequestDto> unbanRequestDtoList) {
        List<String> unbanList = unbanRequestDtoList.stream().map(BanRequestDto::ip).toList();
        List<SuspectIP> suspectIPList = suspectIpRepository.findAllByStatusInAndIpAddressIn(List.of(SuspectIP.IpStatus.BANNED), unbanList);
        String user = UserService.getAuthenticatedUser();
        suspectIPList.forEach(suspectIP -> {
            suspectIP.setStatus(SuspectIP.IpStatus.CANCEL_BAN);
            suspectIP.setStatusAt(new Date());
            suspectIP.setStatusBy(user);
        });
        if (!suspectIPList.isEmpty()) {
            suspectIpRepository.saveAll(suspectIPList);
        }
        List<String> ipListForBanningIp = new ArrayList<>();
        List<String> ipListForFile = new ArrayList<>();
        for (String ip : unbanList) {
            if (banningIpService.isHaveAnyIP(ip)) {
                ipListForBanningIp.add(ip);
            }
            if (fileService.isHaveAnyIp(ip)) {
                ipListForFile.add(ip);
            }
        }
        if (!ipListForBanningIp.isEmpty()) {
            banningIpService.deleteIpAddresses(ipListForBanningIp);
        }
        if (!ipListForFile.isEmpty()) {
            fileService.removeIPAddresses(unbanList);
        }
        return true;
    }

    public boolean isHaveAnyIp(String ipAddress) {
        Optional<SuspectIP> suspectIP = suspectIpRepository.findFirstByIpAddress(ipAddress);
        return suspectIP.isPresent();
    }
}
