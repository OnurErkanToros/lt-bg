package org.lt.project.repository;

import org.lt.project.model.LogListenerRegex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogListenerRegexRepository extends JpaRepository<LogListenerRegex, Integer> {
    List<LogListenerRegex> findByActiveIsTrue();
}
