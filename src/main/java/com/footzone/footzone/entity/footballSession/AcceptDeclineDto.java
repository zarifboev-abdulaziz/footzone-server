package com.footzone.footzone.entity.footballSession;


//Asliddin Kenjaev, created: May, 27 2022 6:45 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcceptDeclineDto {

    private UUID sessionId;

    private Boolean isAccepted;

}
