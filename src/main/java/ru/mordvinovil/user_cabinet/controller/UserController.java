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
            User existingUser = userService.findByEmail(email);

            UserUpdateDto updateDto = new UserUpdateDto();

            if (updates.containsKey("firstName")) {
                updateDto.setFirstName((String) updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                updateDto.setLastName((String) updates.get("lastName"));
            }
            if (updates.containsKey("phoneNumber")) {
                updateDto.setPhoneNumber((String) updates.get("phoneNumber"));
            }
            if (updates.containsKey("dateOfBirth")) {
                updateDto.setDateOfBirth(LocalDate.parse((String) updates.get("dateOfBirth")));
            }

            User userUpdates = convertUpdateDtoToEntity(updateDto, existingUser);

            User updatedUser = userService.updateUser(userUpdates);
            return ResponseEntity.ok(convertToDto(updatedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    private User convertUpdateDtoToEntity(UserUpdateDto dto, User existingUser) {
        User user = new User();
        user.setId(existingUser.getId());
        user.setEmail(existingUser.getEmail());
        user.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : existingUser.getFirstName());
        user.setLastName(dto.getLastName() != null ? dto.getLastName() : existingUser.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : existingUser.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth() : existingUser.getDateOfBirth());

        return user;
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
