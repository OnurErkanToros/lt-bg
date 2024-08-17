package org.lt.project.service;

import org.lt.project.core.convertor.AbuseDbKeyConverter;
import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.dto.resultDto.DataResult;
import org.lt.project.dto.resultDto.ErrorDataResult;
import org.lt.project.dto.resultDto.SuccessDataResult;
import org.lt.project.model.AbuseDBKey;
import org.lt.project.repository.AbuseDBKeyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbuseDBKeyService {
    private final AbuseDBKeyRepository repository;

    public AbuseDBKeyService(AbuseDBKeyRepository repository) {
        this.repository = repository;
    }

    public DataResult<AbuseDbKeyResponseDto> addKey(AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        try{
            AbuseDBKey entity = repository.save(AbuseDbKeyConverter.convert(abuseDbKeyRequestDto));
            return new SuccessDataResult<>(AbuseDbKeyConverter.convert(entity));

        }catch (Exception e){
            return new ErrorDataResult<>("eklenemedi");
        }
    }

    public DataResult<List<AbuseDbKeyResponseDto>> getAllKey() {
        List<AbuseDbKeyResponseDto> abuseDbKeyResponseDtoList =repository.
                findAll()
                .stream()
                .map(AbuseDbKeyConverter::convert).toList();
        if (abuseDbKeyResponseDtoList.isEmpty()){
            return new ErrorDataResult<>("abusedb key listesi boş");
        }else {
            return new SuccessDataResult<>(abuseDbKeyResponseDtoList);
        }
    }

    public DataResult<AbuseDbKeyResponseDto> getLastActiveKey() {
        AbuseDBKey entity = repository.findByIsActive(true).getLast();
        if(entity!=null){
            return new SuccessDataResult<>(AbuseDbKeyConverter.convert(entity));
        }else {
            return new ErrorDataResult<>();
        }
    }
}
