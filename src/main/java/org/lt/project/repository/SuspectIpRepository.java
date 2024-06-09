package org.lt.project.repository;

import org.lt.project.entity.SuspectIPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SuspectIpRepository extends JpaRepository<SuspectIPEntity,Long>{
    List<SuspectIPEntity> findByIpAddress(String ipAddress); 
}
