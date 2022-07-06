package com.footzone.footzone.entity.device;
//Asliddin Kenjaev, created: Jun, 01 2022 2:37 PM

import java.util.UUID;

public interface DeviceProjection {

    UUID getId();

    String getName();

    String getType();

    String getDeviceToken();

}
