package org.lt.project.repository;

import org.lt.project.model.AbuseDBCheckLog;
import org.lt.project.model.SuspectIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbuseDBCheckLogRepository extends JpaRepository<AbuseDBCheckLog, Long> {
    List<AbuseDBCheckLog> findByIpAddress(String ipAddress);

    Optional<AbuseDBCheckLog> findFirstByStatusAndIpAddress(SuspectIP.IpStatus status, String ipAddress);

    List<AbuseDBCheckLog> findAllByIpAddressInAndStatusIn(List<String> ipAddress, List<SuspectIP.IpStatus> status);
}
