package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.model.SuspectIP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbuseDBBlackListRepository extends JpaRepository<AbuseDBBlackList, Long> {
    @NotNull
    Page<AbuseDBBlackList> findAll(@NotNull Pageable pageable);

    List<AbuseDBBlackList> findAllByStatus(SuspectIP.IpStatus status);

    long countAllByStatus(SuspectIP.IpStatus status);
}
