package com.footzone.footzone.entity.footballSession;


import java.util.UUID;



public interface SessionProjection {

    UUID getId();

    String getStadiumName();
    UUID getStadiumId();

    String getStartDate();

    String getStartTime();

    String getEndTime();

    String getStatus();

    Double getHourlyPrice();

    Double getLongitude();

    Double getLatitude();

}
