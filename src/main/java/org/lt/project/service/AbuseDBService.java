package org.lt.project.service;

import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.model.AbuseDBCheckLog;
import org.lt.project.repository.AbuseDBBlackListRepository;
import org.lt.project.repository.AbuseDBCheckLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AbuseDBService {
    private final AbuseDBCheckLogRepository checkLogRepository;
    private final AbuseDBBlackListRepository blackListRepository;

    public AbuseDBService(AbuseDBCheckLogRepository checkLogRepository, AbuseDBBlackListRepository blackListRepository ) {
        this.checkLogRepository = checkLogRepository;
        this.blackListRepository=blackListRepository;
    }

    public AbuseDBCheckLog addAbuseLog(@NonNull AbuseDBCheckLog abuseLog) {
        return checkLogRepository.save(abuseLog);
    }

    public DataResult<AbuseDBCheckLog> getAbuseCheckLogById(long id) {
        Optional<AbuseDBCheckLog> abuseDbOptional = checkLogRepository.findById(id);
        if(abuseDbOptional.isPresent()){
            return new SuccessDataResult<>(abuseDbOptional.get());
        }else{
            return new ErrorDataResult<>("Böyle bir kayıt yok");
        }
    }

    public List<AbuseDBCheckLog> getAllCheckLog() {
        return checkLogRepository.findAll();
    }

    public List<AbuseDBCheckLog> checkLogFindByIpAddress(String ipAddress) {
        return checkLogRepository.findByIpAddress(ipAddress);
    }

    public Result refreshBlackList(List<AbuseBlackListResponseDto> currentBlacklist) {
        List<AbuseDBBlackList> abuseDBBlackListEntityList = currentBlacklist
                    .stream()
                .map(abuseBlackListResponseDto -> AbuseDBBlackList.builder()
                        .ipAddress(abuseBlackListResponseDto.ipAddress())
                        .countryCode(abuseBlackListResponseDto.countryCode())
                        .lastReportedAt(abuseBlackListResponseDto.lastReportedAt())
                        .abuseConfidenceScore(abuseBlackListResponseDto.abuseConfidenceScore())
                        .createdAt(new Date())
                        .createdBy(UserService.getAuthenticatedUser())
                        .isBanned(false)
                        .build())
                    .collect(Collectors.toList());
        List<AbuseDBBlackList> savedBlackList = blackListRepository.saveAll(abuseDBBlackListEntityList);
        if (savedBlackList.isEmpty()) {
                return new ErrorResult("Liste kaydedilemedi.");
            }else{
                return new SuccessResult("Liste eklendi");
            }
    }

    public DataResult<Page<AbuseDBBlackList>> getAllBlackList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AbuseDBBlackList> allBlackList = blackListRepository.findAll(pageable);
        if(allBlackList.isEmpty()){
            return new ErrorDataResult<>("hiç kayıt yok");
        }else {
            return new SuccessDataResult<>(allBlackList);
        }
    }

    public void deleteSuspectIp(long id) {
        Optional<AbuseDBBlackList> blackListEntityOptional = blackListRepository.findById(id);
        blackListEntityOptional.ifPresent(blackListRepository::delete);
    }
}
