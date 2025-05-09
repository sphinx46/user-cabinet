package ru.mordvinovil.user_cabinet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.mordvinovil.user_cabinet.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryUserDAO {
    List<User> USERS = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;

    public InMemoryUserDAO(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return USERS;
    }

    public User registerUser(User user) {
        if (findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        USERS.add(user);
        return user;
    }

    public User updateUser(User user) {
        User existingUser = findByEmail(user.getEmail());
        if (existingUser != null) {
            if (user.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            return existingUser;
        }
        return null;
    }


    public User findByEmail(String email) {
        return USERS.stream()
                .filter(element -> element.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void deleteUser(String email) {
        if (USERS != null && email != null) {
            USERS.removeIf(element -> element.getEmail().equals(email));
        }
    }
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = findByEmail(email);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }
}
