package org.lt.project.service;

import org.lt.project.dto.LogListenerRegexRequestDto;
import org.lt.project.dto.LogListenerRegexResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.LogListenerRegex;
import org.lt.project.repository.LogListenerRegexRepository;
import org.lt.project.util.convertor.LogListenerRegexConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogListenerRegexService {
    private final LogListenerRegexRepository logListenerRegexRepository;

    public LogListenerRegexService(LogListenerRegexRepository logListenerRegexRepository) {
        this.logListenerRegexRepository = logListenerRegexRepository;
    }

    public List<LogListenerRegexResponseDto> getAllPattern() {
        List<LogListenerRegex> allPatternEntity = logListenerRegexRepository.findAll();
        List<LogListenerRegexResponseDto> allPattern = allPatternEntity.stream().map(LogListenerRegexConverter::convert).toList();
        return allPattern;
    }

    public boolean addPattern(LogListenerRegexRequestDto logListenerRegexRequestDto) {
        LogListenerRegexConverter.convert(
                logListenerRegexRepository.save(
                        LogListenerRegexConverter.convert(logListenerRegexRequestDto)));
        return true;

    }

    public boolean deletePattern(int patternId) {
        Optional<LogListenerRegex> currentPattern = logListenerRegexRepository.findById(patternId);
        if (currentPattern.isEmpty()) {
            throw new ResourceNotFoundException("Silinmek istenen pattern bulunamadı.");
        }
        logListenerRegexRepository.delete(currentPattern.get());
        return true;
    }

    public LogListenerRegexResponseDto updatePattern(LogListenerRegexRequestDto listenerPatternRequestDto, int id) {
        Optional<LogListenerRegex> currentPatternEntity = logListenerRegexRepository.findById(id);
        if (currentPatternEntity.isEmpty()) {
            throw new ResourceNotFoundException("Güncellenmek istenen pattern bulunamadı.");
        }
        currentPatternEntity.get().setPattern(listenerPatternRequestDto.pattern());
        currentPatternEntity.get().setExplanation(listenerPatternRequestDto.explanation());
        return LogListenerRegexConverter.convert(logListenerRegexRepository.save(currentPatternEntity.get()));
    }
    public List<LogListenerRegexResponseDto> getActivePatternList(){
        List<LogListenerRegex> activeRegexList = logListenerRegexRepository.findByActiveIsTrue();
        if(activeRegexList.isEmpty()){
            throw new ResourceNotFoundException("aktif regex yok");
        }
        return activeRegexList.stream().map(LogListenerRegexConverter::convert).toList();
    }
}
