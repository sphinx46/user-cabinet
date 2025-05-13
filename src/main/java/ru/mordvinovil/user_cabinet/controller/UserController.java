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

import java.util.List;

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

    @PutMapping("/{email}/update_user")
    public ResponseEntity<?> updateUser(
        @PathVariable String email,
        @Valid @RequestBody UserUpdateDto updateDto) {
        try {
            User user = userService.findByEmail(email);

            if (updateDto.getFirstName() != null) {
                user.setFirstName(updateDto.getFirstName());
            }
            if (updateDto.getLastName() != null) {
                user.setLastName(updateDto.getLastName());
            }
            if (updateDto.getPhoneNumber() != null) {
                user.setPhoneNumber(updateDto.getPhoneNumber());
            }
            if (updateDto.getDateOfBirth() != null) {
                user.setDateOfBirth(updateDto.getDateOfBirth());
            }

            if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
                user.setPassword(updateDto.getPassword());
            } else {
                user.setPassword("1");
            }

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
}
