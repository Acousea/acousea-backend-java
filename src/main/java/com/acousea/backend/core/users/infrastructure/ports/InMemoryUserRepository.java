package com.acousea.backend.core.users.infrastructure.ports;

import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {
    List<User> users = new ArrayList<>(
            List.of(User.createDefault())
    );

    @Override
    public boolean addUser(User user) {
        return users.add(user);
    }

    @Override
    public User getUser(String username) {
        return users.stream().filter(user -> user.username().equals(username)).findFirst().orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return users.stream().filter(user -> user.personalInfo().email().equals(email)).findFirst().orElse(null);
    }

    @Override
    public boolean updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username().equals(user.username())) {
                // Reemplazar el usuario en la lista con el usuario actualizado
                users.set(i, user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return users.removeIf(user -> user.username().equals(username));
    }

    @Override
    public boolean userExists(String username) {
        return users.stream().anyMatch(user -> user.username().equals(username));
    }
}
