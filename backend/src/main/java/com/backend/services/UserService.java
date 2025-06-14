package com.backend.services;

import com.backend.models.Role;
import com.backend.models.User;
import com.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(String name, String email, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole(role != null ? role : Role.USER);
        return userRepo.save(user);
    }

    public User updateUser(UUID id, String name, String email, String password, Role role) {
        User user = getUserById(id);

        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (password != null) user.setPasswordHash(password);
        if (role != null) user.setRole(role);

        return userRepo.save(user);
    }

    public boolean deleteUser(UUID id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
        return true;
    }
}
