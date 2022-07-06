package com.footzone.footzone.entity.footballSession;

import com.footzone.footzone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SessionRequestDto {
    private UUID sessionId;

    private String stadiumName;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Status status;

    private Double hourlyPrice;
}
