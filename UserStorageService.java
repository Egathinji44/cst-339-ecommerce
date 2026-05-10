package com.nexusstore.service;

import com.nexusstore.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserStorageService {
    
    private final ConcurrentHashMap<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public User save(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Long newId = idGenerator.getAndIncrement();
        user.setId(newId);
        userStore.put(newId, user);
        return user;
    }
    
    public Optional<User> findByUsername(String username) {
        return userStore.values().stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }
    
    public Optional<User> findByEmail(String email) {
        return userStore.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
    
    public boolean authenticate(String username, String password) {
        return findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
    
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }
    
    public void updateLastLogin(String username, java.time.LocalDateTime loginTime) {
        findByUsername(username).ifPresent(user -> {
            user.setLastLoginDate(loginTime);
            userStore.put(user.getId(), user);
        });
    }
}