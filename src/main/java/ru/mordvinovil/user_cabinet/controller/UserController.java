package ru.mordvinovil.user_cabinet.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mordvinovil.user_cabinet.exception.UserAlreadyExistsException;
import ru.mordvinovil.user_cabinet.exception.UserNotFoundException;
import ru.mordvinovil.user_cabinet.model.User;
import ru.mordvinovil.user_cabinet.model.dto.*;
import ru.mordvinovil.user_cabinet.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserResponseDto> response = users.stream()
            .map(this::convertToDto)
            .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(convertToDto(user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register_user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = convertToEntity(registrationDto);
            User createdUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

@PatchMapping("/{email}/update_user")
public ResponseEntity<?> partialUpdateUser(
    @PathVariable String email,
    @RequestBody Map<String, Object> updates) {
    try {
        User user = userService.findByEmail(email);

        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            user.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("phoneNumber")) {
            user.setPhoneNumber((String) updates.get("phoneNumber"));
        }
        if (updates.containsKey("dateOfBirth")) {
            user.setDateOfBirth(LocalDate.parse((String) updates.get("dateOfBirth")));
        }

        user.setPassword("1");

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(convertToDto(updatedUser));
    } catch (UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    } catch (UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}


    @PatchMapping("/{email}/password")
    public ResponseEntity<?> changePassword(
        @PathVariable String email,
        @Valid @RequestBody ChangePasswordDto passwordDto) {
        try {
            userService.changePassword(
                email,
                passwordDto.getOldPassword(),
                passwordDto.getNewPassword()
            );
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private User convertToEntity(UserRegistrationDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setPassword(dto.getPassword());
        return user;
    }

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .dateOfBirth(user.getDateOfBirth())
            .build();
    }


    @PostMapping("/login")
    @CrossOrigin(origins = {"http://localhost", "http://127.0.0.1"}, allowCredentials = "true")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            User user = userService.findByEmailWithPassword(loginDto.getEmail());

            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный пароль");
            }
            return ResponseEntity.ok(convertToDto(user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
    }

}
