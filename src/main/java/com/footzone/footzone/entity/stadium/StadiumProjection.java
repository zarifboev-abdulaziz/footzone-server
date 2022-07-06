package com.footzone.footzone.entity.stadium;
//Asliddin Kenjaev, created: Jun, 11 2022 6:48 AM

import com.footzone.footzone.entity.comment.CommentProjection2;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface StadiumProjection {

    UUID getStadiumId();

    String getName();

    double getHourlyPrice();

    double getLongitude();

    double getLatitude();

    boolean getIsActive();

    @Value("#{@stadiumRepository.getAllStadiumPhotosNameByStadiumId(target.stadiumId )}")
    List<String> getPhotos();

    @Value("#{@stadiumService.isOpen(target.stadiumId )}")
    IsOpen getIsOpen();

    @Value("#{@commentRepository.countAllComments(target.stadiumId )}")
    List<CommentProjection2> getComments();

}
