package org.lt.project.service;

import org.lt.project.core.result.DataResult;
import org.lt.project.core.result.ErrorDataResult;
import org.lt.project.core.result.SuccessDataResult;
import org.lt.project.entity.AllSuspectIpEntity;
import org.lt.project.repository.AllSuspectIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllSuspectIpService {
    private final AllSuspectIpRepository allSuspectIpRepository;
    private final AbuseDBService abuseDBService;

    public AllSuspectIpService(AllSuspectIpRepository allSuspectIpRepository, AbuseDBService abuseDBService) {
        this.allSuspectIpRepository = allSuspectIpRepository;
        this.abuseDBService = abuseDBService;
    }

    Optional<AllSuspectIpEntity> getByIpAddress(String ipAddress) {
        return allSuspectIpRepository.getByIpAddress(ipAddress);
    }

    public DataResult<Page<AllSuspectIpEntity>> getAllSuspectIp(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AllSuspectIpEntity> allSuspectIpEntityList = allSuspectIpRepository.findAll(pageable);
        if (allSuspectIpEntityList.isEmpty()) {
            return new ErrorDataResult<>("liste boş");
        } else {
            return new SuccessDataResult<>(allSuspectIpEntityList);
        }
    }

    public DataResult<Page<AllSuspectIpEntity>> getAllSuspectIp(boolean banned,int page,int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<AllSuspectIpEntity> allSuspectByBanStatusAndPageable = allSuspectIpRepository.findAllByBanned(banned,pageable);
        if(allSuspectByBanStatusAndPageable.isEmpty()){
            return new ErrorDataResult<>(String.format("%s listesi boş", banned ? "Banlananlar" : "Banlanmamışlar"));
        }else{
            return new SuccessDataResult<>(allSuspectByBanStatusAndPageable);
        }
    }
    public DataResult<List<AllSuspectIpEntity>> getAllSuspectIp(boolean banned){
        List<AllSuspectIpEntity> allSuspectIpEntityListByBanStatus = allSuspectIpRepository.findAll();
        if(allSuspectIpEntityListByBanStatus.isEmpty()){
            return new ErrorDataResult<>(String.format("%s listesi boş", banned ? "Banlananlar" : "Banlanmamışlar"));
        }else{
            return new SuccessDataResult<>(allSuspectIpEntityListByBanStatus);
        }
    }

    public DataResult<AllSuspectIpEntity> addSuspectIp(AllSuspectIpEntity allSuspectIpEntity) {
        try{
            if (checkDuplicateIp(allSuspectIpEntity.getIpAddress())) {
                AllSuspectIpEntity savedSuspectIp = allSuspectIpRepository.save(allSuspectIpEntity);
                return new SuccessDataResult<>(savedSuspectIp);
            } else {
                return new ErrorDataResult<>("Bu ip zaten listede");
            }
        }catch (Exception e){
            return new ErrorDataResult<>("listeye eklerken bir hata oluştu.");
        }


    }

    public DataResult<List<AllSuspectIpEntity>> addSuspectIp(List<AllSuspectIpEntity> allSuspectIpEntityList) {
        for(AllSuspectIpEntity allSuspectIpEntity:allSuspectIpEntityList){
            if(checkDuplicateIp(allSuspectIpEntity.getIpAddress())){
                allSuspectIpEntityList.remove(allSuspectIpEntity);
                abuseDBService.deleteSuspectIp(allSuspectIpEntity.getId());
            }
        }
        List<AllSuspectIpEntity> savedSuspectIpList = allSuspectIpRepository.saveAll(allSuspectIpEntityList);
        return new SuccessDataResult<>(savedSuspectIpList);
    }
    private boolean checkDuplicateIp(String ip){
        Optional<AllSuspectIpEntity> allSuspectIpEntityOptional = getByIpAddress(ip);
        return allSuspectIpEntityOptional.isPresent();
    }
}
