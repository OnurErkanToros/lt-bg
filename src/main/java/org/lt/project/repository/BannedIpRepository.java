package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.model.AbuseDBBlackList;
import org.lt.project.model.BannedIp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannedIpRepository extends JpaRepository<BannedIp,Long> {
    @NotNull
    Page<BannedIp> findAllByTransferredTrue(@NotNull Pageable pageable);
    List<BannedIp> findByIpIn(List<String> ipList);
    Optional<BannedIp> findByIp(String ip);
    long countAllByTransferredFalse();
}
