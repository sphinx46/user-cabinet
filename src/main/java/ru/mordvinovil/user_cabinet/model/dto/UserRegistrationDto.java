package ru.mordvinovil.user_cabinet.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;


public class UserRegistrationDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    private String password;

    public @Size(min = 8, message = "Password must be at least 8 characters") @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 8, message = "Password must be at least 8 characters") @NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public @NotNull(message = "Date of birth is required") LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull(message = "Date of birth is required") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") @NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Last name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }
}
