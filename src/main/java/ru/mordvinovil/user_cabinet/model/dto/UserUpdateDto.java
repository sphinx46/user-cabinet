package ru.mordvinovil.user_cabinet.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

public class UserUpdateDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    public @NotBlank(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") String lastName) {
        this.lastName = lastName;
    }

    public @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$",
        message = "Phone number should be valid") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull(message = "Date of birth is required") LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull(message = "Date of birth is required") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
