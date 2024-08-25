package org.lt.project.service;

import org.lt.project.core.convertor.SuspectIpConverter;
import org.lt.project.dto.SuspectIpRequestDto;
import org.lt.project.dto.SuspectIpResponseDto;
import org.lt.project.dto.resultDto.*;
import org.lt.project.model.SuspectIP;
import org.lt.project.repository.SuspectIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuspectIpService {
    private final SuspectIpRepository repository;
    private final SuspectIpRepository suspectIpRepository;

    public SuspectIpService(SuspectIpRepository repository, SuspectIpRepository suspectIpRepository) {
        this.repository = repository;
        this.suspectIpRepository = suspectIpRepository;
    }

    public DataResult<Page<SuspectIpResponseDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SuspectIpResponseDto> suspectIpResponseDtos =  repository.findAll(pageable).map(SuspectIpConverter::convert);
        if (!suspectIpResponseDtos.isEmpty()) {
            return new SuccessDataResult<>(suspectIpResponseDtos);
        }else {
            return new ErrorDataResult<>("Şüpheli ip yok");
        }
    }

    public Result save(SuspectIpRequestDto suspectIpRequestDto) {
        SuspectIP suspectIP = SuspectIpConverter.convert(suspectIpRequestDto);
        SuspectIP savedSuspectIP = repository.save(suspectIP);
        if (savedSuspectIP != null) {
            return new SuccessResult();
        }else {
            return new ErrorResult();
        }
    }
}
