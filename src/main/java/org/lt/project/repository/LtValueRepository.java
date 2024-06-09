package org.lt.project.repository;

import org.lt.project.entity.LtValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LtValueRepository extends JpaRepository<LtValueEntity,Long>{
    Optional<LtValueEntity> findByMyKey(String myKey);
}
