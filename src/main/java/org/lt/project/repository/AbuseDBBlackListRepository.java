package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.model.AbuseDBBlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbuseDBBlackListRepository extends JpaRepository<AbuseDBBlackList, Long> {
    @NotNull
    Page<AbuseDBBlackList> findAll(@NotNull Pageable pageable);
    
}
