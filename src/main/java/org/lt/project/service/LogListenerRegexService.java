package org.lt.project.service;

import org.lt.project.dto.LogListenerPatternRequestDto;
import org.lt.project.dto.LogListenerPatternResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.LogListenerPattern;
import org.lt.project.repository.LogListenerRegexRepository;
import org.lt.project.util.convertor.LogListenerPatternConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogListenerRegexService {
    private final LogListenerRegexRepository logListenerRegexRepository;

    public LogListenerRegexService(LogListenerRegexRepository logListenerRegexRepository) {
        this.logListenerRegexRepository = logListenerRegexRepository;
    }

    public List<LogListenerPatternResponseDto> getAllPattern() {
        List<LogListenerPattern> allPatternEntity = logListenerRegexRepository.findAll();
        List<LogListenerPatternResponseDto> allPattern = allPatternEntity.stream().map(LogListenerPatternConverter::convert).toList();
        if (allPattern.isEmpty()) {
            throw new ResourceNotFoundException("Pattern listesi boş.");
        }
        return allPattern;
    }

    public LogListenerPatternResponseDto addPattern(LogListenerPatternRequestDto logListenerPatternRequestDto) {
        return LogListenerPatternConverter.convert(
                logListenerRegexRepository.save(
                        LogListenerPatternConverter.convert(logListenerPatternRequestDto)));

    }

    public boolean deletePattern(int patternId) {
        Optional<LogListenerPattern> currentPattern = logListenerRegexRepository.findById(patternId);
        if (currentPattern.isEmpty()) {
            throw new ResourceNotFoundException("Silinmek istenen pattern bulunamadı.");
        }
        logListenerRegexRepository.delete(currentPattern.get());
        return true;
    }

    public LogListenerPatternResponseDto updatePattern(LogListenerPatternRequestDto listenerPatternRequestDto, int id) {
        Optional<LogListenerPattern> currentPatternEntity = logListenerRegexRepository.findById(id);
        if (currentPatternEntity.isEmpty()) {
            throw new ResourceNotFoundException("Güncellenmek istenen pattern bulunamadı.");
        }
        currentPatternEntity.get().setPattern(listenerPatternRequestDto.pattern());
        currentPatternEntity.get().setExplanation(listenerPatternRequestDto.explanation());
        return LogListenerPatternConverter.convert(logListenerRegexRepository.save(currentPatternEntity.get()));
    }
}
