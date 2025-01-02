package org.lt.project.repository;

import org.lt.project.model.BanningIp;
import org.lt.project.specification.BanningIpSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
public interface BanningIpRepository extends JpaRepository<BanningIp, Long>, JpaSpecificationExecutor<BanningIp> {
    List<BanningIp> findByIpIn(List<String> ipList);

    Optional<BanningIp> findByIp(String ip);
}
