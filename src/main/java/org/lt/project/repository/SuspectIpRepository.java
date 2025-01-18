package org.lt.project.repository;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.lt.project.model.SuspectIP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspectIpRepository extends JpaRepository<SuspectIP, Long>, JpaSpecificationExecutor<SuspectIP> {

    @NotNull
    Page<SuspectIP> findAll(@NotNull Pageable pageable);

    List<SuspectIP> findAllByStatusInAndIpAddressIn(List<SuspectIP.IpStatus> statusList, List<String> ipAddressList);
    Optional<SuspectIP> findFirstByIpAddress(String ipAddress);
}
