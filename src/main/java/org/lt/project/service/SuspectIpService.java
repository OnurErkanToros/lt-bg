package org.lt.project.service;

import org.lt.project.dto.AbuseCheckRequestDto;
import org.lt.project.entity.SuspectIPEntity;
import org.lt.project.repository.SuspectIpRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspectIpService {
    private final SuspectIpRepository repository;
    private final AbuseDBApiService abuseDBApiService;

    public SuspectIpService(SuspectIpRepository repository, AbuseDBApiService abuseDBApiService) {
        this.repository = repository;
        this.abuseDBApiService = abuseDBApiService;
    }

    public List<SuspectIPEntity> getAllSuspectIpList() {
        return repository.findAll();
    }

    public List<SuspectIPEntity> getSuspectIpByIp(String ipAddress) {
        return repository.findByIpAddress(ipAddress);
    }

    public SuspectIPEntity saveSuspectIp(SuspectIPEntity suspectIPEntity) {
        abuseDBApiService.checkIp(90,suspectIPEntity.getIpAddress());
        return repository.save(suspectIPEntity);
    }
}
