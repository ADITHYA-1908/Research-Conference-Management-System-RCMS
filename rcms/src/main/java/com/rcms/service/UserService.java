package com.rcms.service;

import com.rcms.dto.RegisterDto;
import com.rcms.entity.User;
import com.rcms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public User registerAuthor(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered.");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setRole("AUTHOR");
        user.setPhone(registerDto.getPhone());
        user.setInstitution(registerDto.getInstitution());
        user.setBio(registerDto.getBio());
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> findAllAuthors() {
        return userRepository.findByRole("AUTHOR");
    }

    public List<User> findAllReviewers() {
        return userRepository.findByRole("REVIEWER");
    }

    public User updateRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public User updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public User updateProfile(Long id, User updatedInfo) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(updatedInfo.getName());
        user.setPhone(updatedInfo.getPhone());
        user.setInstitution(updatedInfo.getInstitution());
        user.setBio(updatedInfo.getBio());
        return userRepository.save(user);
    }
}
