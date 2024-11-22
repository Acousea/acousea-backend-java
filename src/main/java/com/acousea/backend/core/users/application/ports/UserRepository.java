package com.acousea.backend.core.users.application.ports;


import com.acousea.backend.core.users.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    boolean addUser(User user);
    User getUser(String username);
    boolean updateUser(User user);
    boolean deleteUser(String username);
    boolean userExists(String username);
    User getUserByEmail(String email);
}
