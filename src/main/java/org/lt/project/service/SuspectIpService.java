package org.lt.project.service;

import org.lt.project.core.convertor.SuspectIpConverter;
import org.lt.project.core.result.*;
import org.lt.project.dto.SuspectIpDto;
import org.lt.project.entity.SuspectIPEntity;
import org.lt.project.repository.SuspectIpRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuspectIpService {
    private final SuspectIpRepository repository;
    private final AbuseDBApiService abuseDBApiService;

    public SuspectIpService(SuspectIpRepository repository, AbuseDBApiService abuseDBApiService) {
        this.repository = repository;
        this.abuseDBApiService = abuseDBApiService;
    }

    public DataResult<List<SuspectIpDto>> getAllSuspectIpList() {
        List<SuspectIpDto> suspectIPDtos =
                new ArrayList<>(repository.findAll().stream().map(SuspectIpConverter::convert).toList());
        if (!suspectIPDtos.isEmpty()) {
            return new SuccessDataResult<>(suspectIPDtos);
        }else {
            return new ErrorDataResult<>("Şüpheli ip yok");
        }
    }

    public Result saveSuspectIp(SuspectIpDto suspectIpDto) {
        SuspectIPEntity suspectIPEntity = SuspectIpConverter.convert(suspectIpDto);
        SuspectIPEntity savedSuspectIPEntity = repository.save(suspectIPEntity);
        if(savedSuspectIPEntity!=null){
            return new SuccessResult();
        }else {
            return new ErrorResult();
        }
    }
}
