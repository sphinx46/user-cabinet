package ru.mordvinovil.user_cabinet.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mordvinovil.user_cabinet.model.User;
import ru.mordvinovil.user_cabinet.repository.InMemoryUserDAO;
import ru.mordvinovil.user_cabinet.service.UserService;

import java.util.List;

@Service
public class InMemoryUserServiceImpl implements UserService {
    private final InMemoryUserDAO repository;
    private final PasswordEncoder passwordEncoder;

    public InMemoryUserServiceImpl(InMemoryUserDAO repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmailWithPassword(String email) {
        User user = repository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }


    @Override
    public List<User> findAllUsers() {
        List<User> users = repository.findAllUsers();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    public User registerUser(User user) {
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPhoneNumber(normalizePhoneNumber(user.getPhoneNumber()));
        return repository.registerUser(user);
    }

    private String normalizePhoneNumber(String phone) {
        return phone.replaceAll("[^+0-9]", "");
    }


    @Override
    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        repository.deleteUser(email);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        if (!repository.changePassword(email, oldPassword, newPassword)) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }
}
