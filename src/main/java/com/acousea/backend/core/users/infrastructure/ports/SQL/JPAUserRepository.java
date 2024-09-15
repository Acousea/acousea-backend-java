package com.acousea.backend.core.users.application.ports;

import com.acousea.backend.core.users.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAUserRepository extends JpaRepository<UserEntity, Long> {
}
