package org.lt.project.repository;

import org.lt.project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthenticationRepository extends JpaRepository<UserEntity,Integer>{
    Optional<UserEntity> getByUsername(String username);
}
