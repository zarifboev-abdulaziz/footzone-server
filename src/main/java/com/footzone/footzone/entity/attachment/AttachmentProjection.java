package com.footzone.footzone.entity.attachment;
//Asliddin Kenjaev, created: Jun, 04 2022 7:06 PM

import java.util.UUID;

public interface AttachmentProjection {

    UUID getId();


    String getOriginalName();


    float getSize();


    String getContentType();


    String getName();
}
