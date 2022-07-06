package com.footzone.footzone.entity.stadium;


//Asliddin Kenjaev, created: Jun, 07 2022 3:14 PM 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IsOpen {

    boolean isOpen;

    LocalTime time;

}
