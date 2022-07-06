package com.footzone.footzone.entity.attachment;


//Asliddin Kenjaev, created: Jun, 11 2022 10:03 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachmentDto {

    private UUID photoId;

    private MultipartFile file;

    private boolean changed;

}
