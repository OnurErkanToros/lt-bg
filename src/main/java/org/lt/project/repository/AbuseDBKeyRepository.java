package org.lt.project.repository;

import org.lt.project.entity.AbuseDBKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AbuseDBKeyRepository extends JpaRepository<AbuseDBKeyEntity,Long>{
    List<AbuseDBKeyEntity> findByIsActive(boolean active);
}
