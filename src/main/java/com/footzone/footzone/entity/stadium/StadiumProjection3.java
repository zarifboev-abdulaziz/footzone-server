package com.footzone.footzone.entity.stadium;
//Asliddin Kenjaev, created: Jun, 11 2022 10:56 AM

import java.util.UUID;

public interface StadiumProjection3 {

    UUID getStadiumId();

    String getName();

    double getLongitude();

    double getLatitude();

    boolean getIsActive();

}
