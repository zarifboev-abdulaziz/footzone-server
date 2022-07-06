package com.footzone.footzone.entity.user;


//Asliddin Kenjaev, created: May, 28 2022 2:56 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockUnblockDto {

    private UUID userId;

    private String message;

}
