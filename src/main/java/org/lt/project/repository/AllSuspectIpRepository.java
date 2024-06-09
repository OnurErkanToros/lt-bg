package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.entity.AllSuspectIpEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllSuspectIpRepository extends JpaRepository<AllSuspectIpEntity,Long> {
    Page<AllSuspectIpEntity> findAllByBanned(boolean banned,Pageable pageable);
    Optional<AllSuspectIpEntity> getByIpAddress(String ipAddress);

    @NotNull
    Page<AllSuspectIpEntity> findAll(@NotNull Pageable pageable);

    @NotNull
    List<AllSuspectIpEntity> findAll();
}
