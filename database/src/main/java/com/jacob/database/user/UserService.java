package com.jacob.database.user;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User registerUser(String username, String password) {
        return userRepository.save(new User(username, password));
    }

    @Async
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
