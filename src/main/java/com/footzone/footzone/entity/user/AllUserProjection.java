package com.footzone.footzone.entity.user;


//Asliddin Kenjaev, created: May, 28 2022 3:49 PM 

import java.util.UUID;

public interface AllUserProjection {

    UUID getId();

    String getFullName();

    String getPhoneNumber();

    String getPhotoName();

    boolean getEnabled();

}

