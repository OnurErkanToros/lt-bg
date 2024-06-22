package org.lt.project.service;

import org.lt.project.core.convertor.LogListenerPatternConverter;
import org.lt.project.core.result.*;
import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.entity.LogListenerPatternEntity;
import org.lt.project.repository.LogListenerRegexRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogListenerRegexService {
    private final LogListenerRegexRepository logListenerRegexRepository;

    public LogListenerRegexService(LogListenerRegexRepository logListenerRegexRepository) {
        this.logListenerRegexRepository = logListenerRegexRepository;
    }

    public DataResult<List<LogListenerPatternResponseDto>> getAllPattern() {
        List<LogListenerPatternEntity> allPatternEntity = logListenerRegexRepository.findAll();
        List<LogListenerPatternResponseDto> allPattern = allPatternEntity.stream().map(LogListenerPatternConverter::convert).toList();
        if (allPattern.isEmpty()){
            return new ErrorDataResult<>("liste boş");
        }else {
            return new SuccessDataResult<>(allPattern);
        }
    }

    public Result addPattern(LogListenerPatternRequestDto logListenerPatternRequestDto, String creUser) {
        LogListenerPatternEntity savedLogListenerPatternEntity = logListenerRegexRepository.save(LogListenerPatternConverter.convert(logListenerPatternRequestDto,creUser));
        if(savedLogListenerPatternEntity!=null){
            return new SuccessResult();
        }else{
            return new ErrorResult("ekleme başarısız");
        }
    }
    public Result deletePattern(int patternId){
        Optional<LogListenerPatternEntity> currentPattern = logListenerRegexRepository.findById(patternId);
        if(currentPattern.isPresent()){
            logListenerRegexRepository.delete(currentPattern.get());
            return new SuccessResult("Başarıyla silindi.");
        }else {
            return new ErrorResult("Böyle bir kayıt yok");
        }
    }
    public DataResult<LogListenerPatternResponseDto> updatePattern(LogListenerPatternRequestDto listenerPatternRequestDto,int id){
        Optional<LogListenerPatternEntity> currentPatternEntity = logListenerRegexRepository.findById(id);
        if(currentPatternEntity.isPresent()){
            currentPatternEntity.get().setPattern(listenerPatternRequestDto.getPattern());
            currentPatternEntity.get().setExplanation(listenerPatternRequestDto.getExplanation());
            return new SuccessDataResult<>(LogListenerPatternConverter.convert(logListenerRegexRepository.save(currentPatternEntity.get())));
        }else{
            return new ErrorDataResult<>("Güncelleme başarısız.");
        }
    }
}
