package com.footzone.footzone.entity.user;
//Asliddin Kenjaev, created: May, 28 2022 2:38 PM

import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.role.Role;
import com.footzone.footzone.entity.role.RoleProjection;
import com.footzone.footzone.entity.stadium.Stadium;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserProjection {

    UUID getId();

    String getFullName();

    String getPhoneNumber();

    Set<RoleProjection> getRoles();

    Attachment getPhoto();

    boolean getEnabled();

}
