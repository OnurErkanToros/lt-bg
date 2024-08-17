package org.lt.project.repository;

import org.lt.project.model.LogListenerPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogListenerRegexRepository extends JpaRepository<LogListenerPattern, Integer> {
}
