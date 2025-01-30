package org.lt.project.repository;

import java.util.List;
import java.util.Optional;
import org.lt.project.model.BanningIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BanningIpRepository
    extends JpaRepository<BanningIp, Long>, JpaSpecificationExecutor<BanningIp> {
  List<BanningIp> findByIpIn(List<String> ipList);

  Optional<BanningIp> findFirstByIp(String ip);

  boolean existsByIp(String ip);
}
