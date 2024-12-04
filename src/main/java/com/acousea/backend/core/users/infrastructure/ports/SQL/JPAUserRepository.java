package com.acousea.backend.core.users.infrastructure.ports.SQL;

import com.acousea.backend.core.users.infrastructure.entities.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPAUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u JOIN u.personalInfo pi WHERE pi.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
