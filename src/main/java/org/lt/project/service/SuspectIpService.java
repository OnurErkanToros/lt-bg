package org.lt.project.service;

import org.lt.project.core.convertor.SuspectIpConverter;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.SuspectIP;
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

    public DataResult<List<SuspectIpResponseDto>> getAllSuspectIpList() {
        List<SuspectIpResponseDto> suspectIPRequestDtos =
                new ArrayList<>(repository.findAll().stream().map(SuspectIpConverter::convert).toList());
        if (!suspectIPRequestDtos.isEmpty()) {
            return new SuccessDataResult<>(suspectIPRequestDtos);
        }else {
            return new ErrorDataResult<>("Şüpheli ip yok");
        }
    }

    public Result saveSuspectIp(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        SuspectIP savedSuspectIP = repository.save(suspectIP);
        if (savedSuspectIP != null) {
            return new SuccessResult();
        }else {
            return new ErrorResult();
        }
    }
}
