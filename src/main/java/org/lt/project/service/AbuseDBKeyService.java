package org.lt.project.service;

import org.lt.project.dto.AbuseDbKeyRequestDto;
import org.lt.project.dto.AbuseDbKeyResponseDto;
import org.lt.project.exception.customExceptions.ResourceNotFoundException;
import org.lt.project.model.AbuseDBKey;
import org.lt.project.repository.AbuseDBKeyRepository;
import org.lt.project.util.convertor.AbuseDbKeyConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbuseDBKeyService {
    private final AbuseDBKeyRepository repository;

    public AbuseDBKeyService(AbuseDBKeyRepository repository) {
        this.repository = repository;
    }

    public AbuseDbKeyResponseDto addKey(AbuseDbKeyRequestDto abuseDbKeyRequestDto) {
        AbuseDBKey entity = repository.save(AbuseDbKeyConverter.convert(abuseDbKeyRequestDto));
        return AbuseDbKeyConverter.convert(entity);
    }

    public List<AbuseDbKeyResponseDto> getAllKey() {

        return repository.
                findAll()
                .stream()
                .map(AbuseDbKeyConverter::convert).toList();
    }

    public AbuseDbKeyResponseDto getLastActiveKey() {
        List<AbuseDBKey> activeKeys = repository.findByIsActive(true);
        if (activeKeys.isEmpty()) {
            throw new ResourceNotFoundException("Key yok!");
        }
        return AbuseDbKeyConverter.convert(activeKeys.getLast());
    }

    public boolean deleteAbuseKey(long id) {
        Optional<AbuseDBKey> abuseDBKeyOptional = repository.findById(id);
        if (abuseDBKeyOptional.isPresent()) {
            repository.delete(abuseDBKeyOptional.get());
            return true;
        } else {
            throw new ResourceNotFoundException("İlgili key bulunamadı!");
        }
    }
}
