package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.model.SuspectIP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SuspectIpRepository extends JpaRepository<SuspectIP, Long> {
    List<SuspectIP> findByIpAddress(String ipAddress);
    @NotNull
    Page<SuspectIP> findAll(@NotNull Pageable pageable);
    List<SuspectIP> findAllByBannedFalseAndIpAddressIn(List<String> ipAddressList);
}
