package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Spring Data MongoDB implementation of UserService.
 * Passwords are BCrypt-encoded before storage (Milestone 6).
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean registerUser(UserModel user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        user.setId(null);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
}
