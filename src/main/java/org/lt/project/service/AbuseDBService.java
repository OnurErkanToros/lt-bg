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
    private final FileService fileService;


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


    public boolean setBanForCheckIp(BanRequestDto banRequestDto) {
        var existingCheckLogList =
                checkLogRepository.findAllByIpAddressInAndStatusIn(List.of(banRequestDto.ip()), List.of(SuspectIP.IpStatus.NEW, SuspectIP.IpStatus.CANCEL_BAN));
        var user = UserService.getAuthenticatedUser();
        if (!existingCheckLogList.isEmpty()) {
            existingCheckLogList.forEach(abuseDBCheckLog -> {
                abuseDBCheckLog.setStatusBy(user);
                abuseDBCheckLog.setStatusAt(new Date());
                abuseDBCheckLog.setStatus(SuspectIP.IpStatus.BANNED);
            });
            checkLogRepository.saveAll(existingCheckLogList);
        }
        if (!banningIpService.isHaveAnyIP(banRequestDto.ip())) {
            banningIpService.addBannedIp(
                    BanningIp.builder()
                            .ip(banRequestDto.ip())
                            .ipType(BanningIp.BanningIpType.CHECK)
                            .createdAt(new Date())
                            .createdBy(user)
                            .build());
        }
        if (!fileService.isHaveAnyIp(banRequestDto.ip())) {
            fileService.addIPAddress(banRequestDto.ip());
        }
        return true;

    }

    public Boolean setUnbanForCheckIp(List<BanRequestDto> unbanRequestDtoList) {
        List<String> ipList = unbanRequestDtoList.stream().map(BanRequestDto::ip).toList();
        var user = UserService.getAuthenticatedUser();
        List<AbuseDBCheckLog> existingCheckLogList =
                checkLogRepository.findAllByIpAddressInAndStatusIn(ipList, List.of(SuspectIP.IpStatus.BANNED));

        if (!existingCheckLogList.isEmpty()) {
            existingCheckLogList.forEach(abuseDBCheckLog -> {
                abuseDBCheckLog.setStatusBy(user);
                abuseDBCheckLog.setStatusAt(new Date());
                abuseDBCheckLog.setStatus(SuspectIP.IpStatus.CANCEL_BAN);
            });
            checkLogRepository.saveAll(existingCheckLogList);
        }
        List<String> ipListForBanningIp = new ArrayList<>();
        List<String> ipListForFile = new ArrayList<>();
        for (String ip : ipList) {
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
            fileService.removeIPAddresses(ipListForFile);
        }
        return true;
    }

    public boolean isIpAddressBanned(String ipAddress) {
        return checkLogRepository.findFirstByStatusAndIpAddress(SuspectIP.IpStatus.BANNED, ipAddress).isPresent();
    }


}
