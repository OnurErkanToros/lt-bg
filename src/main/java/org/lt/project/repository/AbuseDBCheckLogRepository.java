package org.lt.project.repository;

import org.lt.project.model.AbuseDBCheckLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbuseDBCheckLogRepository extends JpaRepository<AbuseDBCheckLog, Long> {
    List<AbuseDBCheckLog> findByIpAddress(String ipAddress);
    Optional<AbuseDBCheckLog> findByBannedFalseAndIpAddress(String ipAddress);
    List<AbuseDBCheckLog> findAllByIpAddressAndBannedFalse(String ipAddress);
}
