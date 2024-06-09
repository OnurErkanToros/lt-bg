package org.lt.project.service;

import org.lt.project.core.result.*;
import org.lt.project.dto.AbuseBlackListResponseDto;
import org.lt.project.entity.AbuseDBBlackListEntity;
import org.lt.project.entity.AbuseDBLogEntity;
import org.lt.project.repository.AbuseDBBlackListRepository;
import org.lt.project.repository.AbuseDBCheckLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
    
    public AbuseDBLogEntity addAbuseLog(@NonNull AbuseDBLogEntity abuseLog){
        return checkLogRepository.save(abuseLog);
    }
    public DataResult<AbuseDBLogEntity> getAbuseCheckLogById(long id){
        Optional<AbuseDBLogEntity> abuseDbOptional = checkLogRepository.findById(id);
        if(abuseDbOptional.isPresent()){
            return new SuccessDataResult<>(abuseDbOptional.get());
        }else{
            return new ErrorDataResult<>("Böyle bir kayıt yok");
        }
    }
    public List<AbuseDBLogEntity> getAllCheckLog(){
        return checkLogRepository.findAll();
    }
    public List<AbuseDBLogEntity> checkLogFindByIpAddress(String ipAddress){
        return checkLogRepository.findByIpAddress(ipAddress);
    }

    public Result refreshBlackList(List<AbuseBlackListResponseDto> currentBlacklist) {
        List<AbuseDBBlackListEntity> abuseDBBlackListEntityList = currentBlacklist
                    .stream()
                .map(abuseBlackListResponseDto -> new AbuseDBBlackListEntity(
                        abuseBlackListResponseDto.getIpAddress(),
                        abuseBlackListResponseDto.getLastReportedAt(),
                        abuseBlackListResponseDto.getCountryCode()))
                    .collect(Collectors.toList());
            List<AbuseDBBlackListEntity> savedBlackList = blackListRepository.saveAll(abuseDBBlackListEntityList);
        if (savedBlackList.isEmpty()) {
                return new ErrorResult("Liste kaydedilemedi.");
            }else{
                return new SuccessResult("Liste eklendi");
            }
    }

    public DataResult<Page<AbuseDBBlackListEntity>> getAllBlackList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AbuseDBBlackListEntity> allBlackList = blackListRepository.findAll(pageable);
        if(allBlackList.isEmpty()){
            return new ErrorDataResult<>("hiç kayıt yok");
        }else {
            return new SuccessDataResult<>(allBlackList);
        }
    }

    public void deleteSuspectIp(long id) {
        Optional<AbuseDBBlackListEntity> blackListEntityOptional = blackListRepository.findById(id);
        blackListEntityOptional.ifPresent(blackListRepository::delete);
    }
}
