package org.lt.project.service;

import org.lt.project.entity.LtValueEntity;
import org.lt.project.repository.LtValueRepository;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.Optional;

@Service
public class LtValueService {
    private final LtValueRepository repository;

    public LtValueService(LtValueRepository repository) {
        this.repository = repository;
    }
    
    public LtValueEntity getValueByKey(String key) throws Exception{
        Optional<LtValueEntity> ltValueOptional =  repository.findByMyKey(key);
        if(ltValueOptional.isPresent()){
            return ltValueOptional.get();
        }
        throw new RuntimeErrorException(new Error("Böyle bir key yok"));
    }
}
