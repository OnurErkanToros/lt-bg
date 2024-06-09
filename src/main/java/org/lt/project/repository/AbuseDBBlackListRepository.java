package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.entity.AbuseDBBlackListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbuseDBBlackListRepository extends JpaRepository<AbuseDBBlackListEntity,Long>{
    @NotNull
    Page<AbuseDBBlackListEntity> findAll(@NotNull Pageable pageable);
    
}
