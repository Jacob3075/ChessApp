package com.jacob.database.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }

    public User registerUser(String username, String password) {
        return userRepository.save(new User(username, password));
    }
}
