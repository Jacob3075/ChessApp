package com.jacob.database.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(@NotNull User user) {
        return userRepository
                .findByUsernameAndPassword(user.getUsername(), user.getPassword())
                .isPresent();
    }

    public User registerUser(String username, String password) {
        return userRepository.save(new User(username, password));
    }
}
