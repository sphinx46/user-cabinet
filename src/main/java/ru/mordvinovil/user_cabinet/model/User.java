package ru.mordvinovil.user_cabinet.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;


@Entity
@Table(name = "users")

public class User {
    @GeneratedValue
    @Id
    private Long id;

    @NotBlank(message = "First name is not filled.")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is not filled.")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters.")
    private String lastName;

    @NotBlank(message = "Email is not filled.")
    @Email(message = "Email should be valid.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is not filled.")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{10,20}$", message = "Phone number should be valid.")
    @Column(unique = true)
    private String phoneNumber;

    @DateTimeFormat
    private LocalDate dateOfBirth;

    private String password;

    @Transient
    @JsonIgnore
    private String plainPassword;

    @Transient
    private int age;

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(@NotBlank(message = "First name is not filled.") @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters.") String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank(message = "Last name is not filled.") @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters.") String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(@NotBlank(message = "Email is not filled.") @Email(message = "Email should be valid.") String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank(message = "First name is not filled.") @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters.") String getFirstName() {
        return firstName;
    }

    public @NotBlank(message = "Last name is not filled.") @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters.") String getLastName() {
        return lastName;
    }

    public @NotBlank(message = "Email is not filled.") @Email(message = "Email should be valid.") String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }


    public String getPlainPassword() {
        return plainPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
