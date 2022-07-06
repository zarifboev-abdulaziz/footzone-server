package com.footzone.footzone.entity.stadium;
//Sevinch Abdisattorova 05/28/2022 12:53 PM

import com.footzone.footzone.entity.workingDay.WorkingDayDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class StadiumDto {

    @NotNull
    @Size(min = 3, message = "Stadium name should contain at least 3 letters")
    private String name;

    private String number;

    @NotNull
    private Double hourlyPrice;

    private String address;

    private double longitude;

    private double latitude;

    private UUID userId;
    List<WorkingDayDto> workingDays;

}
