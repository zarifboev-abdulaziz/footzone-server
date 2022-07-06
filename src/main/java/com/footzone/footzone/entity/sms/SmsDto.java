package com.footzone.footzone.entity.sms;


//Asliddin Kenjaev, created: May, 13 2022 10:40 AM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsDto {

    @NotNull(message = "Phone number cannot be null.")
    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @NotNull(message = "code cannot be null.")
//    @NotBlank(message = "code cannot be blank.")
    private String codeSent;

}
