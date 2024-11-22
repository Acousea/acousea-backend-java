package com.acousea.backend.core.users.infrastructure.ports.SQL;

import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.infrastructure.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SQLUserRepository implements UserRepository {
    private final JPAUserRepository jpaUserRepository;

    @Autowired
    public SQLUserRepository(JPAUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public boolean addUser(User user) {
        try {
            jpaUserRepository.save(UserEntity.fromDomain(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        Optional<UserEntity> user = jpaUserRepository.findByUsername(username);
        return user.map(UserEntity::toDomain).orElse(null);
    }


    @Override
    public User getUserByEmail(String email) {
        Optional<UserEntity> user = jpaUserRepository.findByEmail(email);
        return user.map(UserEntity::toDomain).orElse(null);
    }

    @Override
    public boolean updateUser(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        if (jpaUserRepository.existsById(userEntity.getId())) {
            jpaUserRepository.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        Optional<UserEntity> user = jpaUserRepository.findByUsername(username);
        if (user.isPresent()) {
            jpaUserRepository.delete(user.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean userExists(String username) {
        return jpaUserRepository.existsByUsername(username);
    }
}
