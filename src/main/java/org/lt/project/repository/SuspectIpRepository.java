package org.lt.project.repository;

import org.lt.project.model.SuspectIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SuspectIpRepository extends JpaRepository<SuspectIP, Long> {
    List<SuspectIP> findByIpAddress(String ipAddress);
}
