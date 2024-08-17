package org.lt.project.repository;

import org.lt.project.model.AbuseDBKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AbuseDBKeyRepository extends JpaRepository<AbuseDBKey, Long> {
    List<AbuseDBKey> findByIsActive(boolean active);
}
