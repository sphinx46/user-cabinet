package ru.mordvinovil.user_cabinet.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


public class ChangePasswordDto {
    @NotBlank(message = "Current password is required")
    private String oldPassword;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "New password is required")
    private String newPassword;

    public @NotBlank(message = "Current password is required") String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank(message = "Current password is required") String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @Size(min = 8, message = "Password must be at least 8 characters") @NotBlank(message = "New password is required") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@Size(min = 8, message = "Password must be at least 8 characters") @NotBlank(message = "New password is required") String newPassword) {
        this.newPassword = newPassword;
    }
}
