package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.AbuseDBBlackListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AbuseDBService {
    private final AbuseDBBlackListRepository blackListRepository;
    private final BanningIpService banningIpService;
    private final FileService fileService;


    public boolean refreshBlackList(List<AbuseBlackListResponseDto> currentBlacklist) {

        List<AbuseDBBlackList> abuseDBBlackListEntityList = currentBlacklist
                .stream()
                .map(abuseBlackListResponseDto -> AbuseDBBlackList.builder()
                        .ipAddress(abuseBlackListResponseDto.ipAddress())
                        .countryCode(abuseBlackListResponseDto.countryCode())
                        .lastReportedAt(abuseBlackListResponseDto.lastReportedAt())
                        .abuseConfidenceScore(abuseBlackListResponseDto.abuseConfidenceScore())
                        .createdAt(new Date())
                        .createdBy(UserService.getAuthenticatedUser())
                        .status(SuspectIP.IpStatus.NEW)
                        .statusBy(UserService.getAuthenticatedUser())
                        .statusAt(new Date())
                        .build())
                .collect(Collectors.toList());
        List<AbuseDBBlackList> savedBlackList = blackListRepository.saveAll(abuseDBBlackListEntityList);
        return !savedBlackList.isEmpty();
    }

    public Page<AbuseDBBlackList> getAllBlackList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AbuseDBBlackList> allBlackList = blackListRepository.findAll(pageable);
        if (allBlackList.isEmpty()) {
            throw new ResourceNotFoundException("Blacklist kaydı yok.");
        }
        return allBlackList;
    }

    public Long getCountBlacklistNewStatus() {
        return blackListRepository.countAllByStatus(SuspectIP.IpStatus.NEW);
    }



    public boolean setBanForBlacklist() {
        List<AbuseDBBlackList> existingBlackList = blackListRepository.findAllByStatus(SuspectIP.IpStatus.NEW);
        if (existingBlackList.isEmpty()) {
            throw new ResourceNotFoundException("Banlanacak ip yok.");
        }

        List<BanningIp> banningIpList = new ArrayList<>();
        for (AbuseDBBlackList dbBlackList : existingBlackList) {
            dbBlackList.setStatusBy(UserService.getAuthenticatedUser());
            dbBlackList.setStatusAt(new Date());
            banningIpList.add(BanningIp.builder()
                    .ip(dbBlackList.getIpAddress())
                    .ipType(BanningIp.BanningIpType.BLACKLIST)
                    .createdAt(new Date())
                    .createdBy(UserService.getAuthenticatedUser())
                    .build());
        }
        banningIpService.addAllIp(banningIpList);
        return !blackListRepository.saveAll(existingBlackList).isEmpty();
    }

}
