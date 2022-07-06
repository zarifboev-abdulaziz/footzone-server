package com.footzone.footzone.authService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @NotNull(message = "code cannot be null.")
    private String codeSent;

    @NotNull(message = "Field is required")
    @NotBlank(message = "Device name cannot be blank.")
    private String deviceName;

    private String deviceType = "mobile";

    @NotNull(message = "Field is required")
    @NotBlank(message = "Device token cannot be blank.")
    private String deviceToken;


}
