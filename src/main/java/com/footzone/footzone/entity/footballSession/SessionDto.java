package com.footzone.footzone.entity.footballSession;


//Asliddin Kenjaev, created: May, 27 2022 5:57 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionDto {

    private UUID stadiumId;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalTime endTime;
}
