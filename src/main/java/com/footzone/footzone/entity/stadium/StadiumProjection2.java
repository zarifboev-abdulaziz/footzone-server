package com.footzone.footzone.entity.stadium;
//Asliddin Kenjaev, created: Jun, 11 2022 7:55 AM

import com.footzone.footzone.entity.attachment.AttachmentProjection;
import com.footzone.footzone.entity.comment.CommentProjection;
import com.footzone.footzone.entity.workingDay.WorkingDayProjection2;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface StadiumProjection2 {

    UUID getStadiumId();

    String getStadiumName();

    String getNumber();

    String getAddress();

    double getHourlyPrice();

    double getLongitude();

    double getLatitude();

    @Value("#{@attachmentRepository.getAllStadiumPhotosNameByStadiumId(target.stadiumId )}")
    List<AttachmentProjection> getPhotos();

    @Value("#{@stadiumService.isOpen(target.stadiumId )}")
    IsOpen getIsOpen();

    @Value("#{@workingDayRepository.findWorkingDaysByStadiumId(target.stadiumId )}")
    List<WorkingDayProjection2> getWorkingDays();

}
