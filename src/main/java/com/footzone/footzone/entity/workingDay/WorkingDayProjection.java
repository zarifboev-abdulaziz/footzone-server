package com.footzone.footzone.entity.workingDay;
//Asliddin Kenjaev, created: Jun, 04 2022 7:03 PM

import com.footzone.footzone.entity.time.TimeProjection;

public interface WorkingDayProjection {

    String getDayName();

    TimeProjection getStartTime();

    TimeProjection getEndTime();
}
