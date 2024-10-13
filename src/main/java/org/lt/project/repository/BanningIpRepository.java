package org.lt.project.repository;

import org.jetbrains.annotations.NotNull;
import org.lt.project.model.BanningIp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface BanningIpRepository extends JpaRepository<BanningIp, Long> {
    @NotNull
    Page<BanningIp> findAllByStatus(BanningIp.BanningIpStatus status, @NotNull Pageable pageable);

    List<BanningIp> findAllByStatus(BanningIp.BanningIpStatus status);

    List<BanningIp> findByIpIn(List<String> ipList);

    Optional<BanningIp> findByIp(String ip);

    long countAllByStatus(BanningIp.BanningIpStatus status);
}
