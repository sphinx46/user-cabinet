package ru.mordvinovil.user_cabinet.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mordvinovil.user_cabinet.model.User;
import ru.mordvinovil.user_cabinet.repository.InMemoryUserDAO;
import ru.mordvinovil.user_cabinet.service.UserService;

import java.util.List;

@Service
public class InMemoryUserServiceImpl implements UserService {
    private final InMemoryUserDAO repository;

    public InMemoryUserServiceImpl(InMemoryUserDAO repository) {
        this.repository = repository;
    }


    @Override
    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }

    @Override
    public User registerUser(User user) {
        return repository.registerUser(user);
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