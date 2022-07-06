package com.footzone.footzone.entity.time;

import java.time.LocalTime;
import java.util.UUID;

public interface TimeProjection  {
    UUID getTimeId();
    LocalTime getTime();

}
