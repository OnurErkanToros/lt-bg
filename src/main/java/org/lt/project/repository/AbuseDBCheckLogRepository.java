package org.lt.project.repository;

import org.lt.project.entity.AbuseDBLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbuseDBCheckLogRepository extends JpaRepository<AbuseDBLogEntity,Long>{
    List<AbuseDBLogEntity> findByIpAddress(String ipAddress);
}
