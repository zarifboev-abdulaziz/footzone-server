package com.footzone.footzone.entity.footballSession;


//Asliddin Kenjaev, created: Jun, 23 2022 11:09 AM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionDto2 {

    private String sessionId;

    private String startDate;

    private String startTime;

    private String endTime;

    private String stadiumName;

    private boolean stadiumHolder;
}
