package com.footzone.footzone.authService;

import com.footzone.footzone.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull(message = "Field is required")
    @NotBlank(message = "Full Name cannot be blank.")
    private String fullName;

    @NotNull(message = "Field is required")
    @NotBlank(message = "Phone Number cannot be blank.")
    private String phoneNumber;

    @NotNull(message = "Field is required")
    @NotBlank(message = "SMS code cannot be blank.")
    private String smsCode;

    @NotNull(message = "Field is required")
//    @NotBlank(message = "Language cannot be blank.")
    private Language language;

    @NotNull(message = "Field is required")
    private boolean stadiumHolder;

    @NotNull(message = "Field is required")
    @NotBlank(message = "Device name cannot be blank.")
    private String deviceName;

//    @NotNull(message = "Field is required")
//    @NotBlank(message = "Device type cannot be blank.")
    private String deviceType;

    @NotNull(message = "Field is required")
    @NotBlank(message = "Device token cannot be blank.")
    private String deviceToken;

}
