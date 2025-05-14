package ru.mordvinovil.user_cabinet.service;

import ru.mordvinovil.user_cabinet.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    User registerUser(User user);
    User updateUser(User user);
    User findByEmail(String email);
    void deleteUser(String email);
    User findByEmailWithPassword(String email);
    void changePassword(String email, String oldPassword, String newPassword);
}
