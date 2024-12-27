package org.lt.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.BanRequestDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.ErrorDataResult;
import org.lt.project.dto.resultDto.SuccessDataResult;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.model.AbuseDBCheckLog;
import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.AbuseDBBlackListRepository;
import org.lt.project.repository.AbuseDBCheckLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AbuseDBService {
    private final AbuseDBCheckLogRepository checkLogRepository;
    private final AbuseDBBlackListRepository blackListRepository;
    private final BanningIpService banningIpService;


    public AbuseDBCheckLog addAbuseLog(@NonNull AbuseDBCheckLog abuseLog) {
        return checkLogRepository.save(abuseLog);
    }

    public DataResult<AbuseDBCheckLog> getAbuseCheckLogById(long id) {
        Optional<AbuseDBCheckLog> abuseDbOptional = checkLogRepository.findById(id);
        if (abuseDbOptional.isPresent()) {
            return new SuccessDataResult<>(abuseDbOptional.get());
        } else {
            return new ErrorDataResult<>("Böyle bir kayıt yok");
        }
    }

    public List<AbuseDBCheckLog> getAllCheckLog() {
        return checkLogRepository.findAll();
    }

    public List<AbuseDBCheckLog> checkLogFindByIpAddress(String ipAddress) {
        return checkLogRepository.findByIpAddress(ipAddress);
    }

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

    public void deleteSuspectIp(long id) {
        Optional<AbuseDBBlackList> blackListEntityOptional = blackListRepository.findById(id);
        blackListEntityOptional.ifPresent(blackListRepository::delete);
    }

    public boolean setBanForBlacklist() {
        List<AbuseDBBlackList> existingBlackList = blackListRepository.findAllByStatus(SuspectIP.IpStatus.NEW);
        if (existingBlackList.isEmpty()) {
            throw new ResourceNotFoundException("Banlanacak ip yok.");
        }

        List<BanningIp> banningIpList = new ArrayList<>();
        for (AbuseDBBlackList dbBlackList : existingBlackList) {
            dbBlackList.setStatus(SuspectIP.IpStatus.READY_TRANSFER);
            dbBlackList.setStatusBy(UserService.getAuthenticatedUser());
            dbBlackList.setStatusAt(new Date());
            banningIpList.add(BanningIp.builder()
                    .ip(dbBlackList.getIpAddress())
                    .ipType(BanningIp.BanningIpType.BLACKLIST)
                    .status(BanningIp.BanningIpStatus.NOT_TRANSFERRED)
                    .createdAt(new Date())
                    .createdBy(UserService.getAuthenticatedUser())
                    .build());
        }
        banningIpService.addAllIp(banningIpList);
        return !blackListRepository.saveAll(existingBlackList).isEmpty();
    }


    public boolean setBanForCheckIp(BanRequestDto banRequestDto) {
        List<AbuseDBCheckLog> existingCheckLogList =
                checkLogRepository.findAllByIpAddressAndStatus(banRequestDto.ip(), SuspectIP.IpStatus.NEW);
        if (existingCheckLogList.isEmpty()) {
            throw new ResourceNotFoundException("Banlanacak ip yok.");
        }
        existingCheckLogList.forEach(abuseDBCheckLog -> {
            abuseDBCheckLog.setStatus(SuspectIP.IpStatus.READY_TRANSFER);
            abuseDBCheckLog.setStatusBy(UserService.getAuthenticatedUser());
            abuseDBCheckLog.setStatusAt(new Date());
        });
        checkLogRepository.saveAll(existingCheckLogList);
        banningIpService.addBannedIp(
                BanningIp.builder()
                        .ip(banRequestDto.ip())
                        .ipType(BanningIp.BanningIpType.CHECK)
                        .status(BanningIp.BanningIpStatus.NOT_TRANSFERRED)
                        .createdAt(new Date())
                        .createdBy(UserService.getAuthenticatedUser())
                        .build());
        return true;

    }
}
