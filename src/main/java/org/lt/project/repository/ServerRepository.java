package org.lt.project.repository;

import org.lt.project.entity.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity,Integer> {
    long countByIdIn(List<Integer> ids);
}
