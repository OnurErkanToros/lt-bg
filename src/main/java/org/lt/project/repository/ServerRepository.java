package org.lt.project.repository;

import org.lt.project.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Integer> {
    long countByIdIn(List<Integer> ids);
}
