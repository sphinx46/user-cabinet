package ru.mordvinovil.user_cabinet.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mordvinovil.user_cabinet.exception.UserAlreadyExistsException;
import ru.mordvinovil.user_cabinet.exception.UserNotFoundException;
import ru.mordvinovil.user_cabinet.model.User;
import ru.mordvinovil.user_cabinet.repository.UserRepository;
import ru.mordvinovil.user_cabinet.service.UserService;

import java.util.List;

@Primary
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = repository.findAll();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email "
                    + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String normalizedPhone = normalizePhoneNumber(user.getPhoneNumber());
        user.setPhoneNumber(normalizedPhone);
        return repository.save(user);
    }

    private String normalizePhoneNumber(String phone) {
        return phone.replaceAll("[^+0-9]", "");
    }

    @Override
    @Transactional
    public User updateUser(User userUpdates) {
        User existingUser = repository.findByEmail(userUpdates.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Обновляем только те поля, которые пришли в запросе
        if (userUpdates.getFirstName() != null) {
            existingUser.setFirstName(userUpdates.getFirstName());
        }
        if (userUpdates.getLastName() != null) {
            existingUser.setLastName(userUpdates.getLastName());
        }
        if (userUpdates.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userUpdates.getDateOfBirth());
        }
        if (userUpdates.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(normalizePhoneNumber(userUpdates.getPhoneNumber()));
        }

        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }

        return repository.save(existingUser);
    }

    @Override
    public User findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        if (!repository.existsByEmail(email)) {
            throw new UserNotFoundException("User not found");
        }
        repository.deleteByEmail(email);
    }

    public User findByEmailWithPassword(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }
}
