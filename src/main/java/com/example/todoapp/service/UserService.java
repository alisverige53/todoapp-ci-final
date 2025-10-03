package com.example.todoapp.service;

import com.example.todoapp.model.User;
import com.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        return userRepo.save(user);
    }


    public void delete(Long id) {
        userRepo.deleteById(id);
    }
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
