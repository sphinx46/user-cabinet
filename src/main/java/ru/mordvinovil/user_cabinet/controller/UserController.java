package ru.mordvinovil.user_cabinet.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mordvinovil.user_cabinet.model.User;
import ru.mordvinovil.user_cabinet.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> findAllUsers() {
        List<User> users = service.findAllUsers();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @PostMapping("register_user")
    public String registerUser(@Valid @RequestBody User user) {
        service.registerUser(user);
        return "User successfully register.";
    }

    @PutMapping("update_user")
    public String updateUser(@RequestBody User user) {
        service.updateUser(user);
        return "User successfully updated.";
    }

    @GetMapping("/{email}")
    public User findByEmail(@PathVariable  String email) {
        User user = service.findByEmail(email);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }


    @DeleteMapping("delete_user/{email}")
    public void deleteUser(@PathVariable String email) {
        service.deleteUser(email);
    }

    @PutMapping("change_password/{email}/{oldPassword}/{newPassword}")
    public String changePassword(
            @PathVariable String email,
            @PathVariable String oldPassword,
            @PathVariable String newPassword) {
        service.changePassword(email, oldPassword, newPassword);
        return "Password successfully changed.";
    }
}
