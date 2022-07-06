package com.footzone.footzone.entity.user;


//Asliddin Kenjaev, created: May, 30 2022 12:37 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEditDto {

    private String fullName;

    private String phoneNumber;

}
