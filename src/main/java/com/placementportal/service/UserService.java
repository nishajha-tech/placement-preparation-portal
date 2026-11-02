package com.placementportal.service;

import com.placementportal.entity.User;
import com.placementportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service handling Student & User management, registration, and profile updates.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new student with BCrypt password encryption.
     */
    @Transactional
    public User registerStudent(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("An account with email " + user.getEmail() + " already exists.");
        }

        // Encrypt plain password using BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_STUDENT");
        
        // Defaults for academic details if not supplied
        if (user.getActiveBacklogs() == null) {
            user.setActiveBacklogs(0);
        }
        if (user.getCgpa() == null) {
            user.setCgpa(7.5);
        }
        if (user.getTenthPercentage() == null) {
            user.setTenthPercentage(75.0);
        }
        if (user.getTwelfthPercentage() == null) {
            user.setTwelfthPercentage(75.0);
        }

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllStudents() {
        return userRepository.findByRole("ROLE_STUDENT");
    }

    public long countStudents() {
        return userRepository.countByRole("ROLE_STUDENT");
    }

    /**
     * Update student academic profile.
     */
    @Transactional
    public User updateProfile(User existingUser, User updatedData) {
        existingUser.setFullName(updatedData.getFullName());
        existingUser.setPhone(updatedData.getPhone());
        existingUser.setCollege(updatedData.getCollege());
        existingUser.setDegree(updatedData.getDegree());
        existingUser.setBranch(updatedData.getBranch());
        existingUser.setCgpa(updatedData.getCgpa());
        existingUser.setTenthPercentage(updatedData.getTenthPercentage());
        existingUser.setTwelfthPercentage(updatedData.getTwelfthPercentage());
        existingUser.setActiveBacklogs(updatedData.getActiveBacklogs());
        existingUser.setPassingYear(updatedData.getPassingYear());
        existingUser.setSkills(updatedData.getSkills());

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
