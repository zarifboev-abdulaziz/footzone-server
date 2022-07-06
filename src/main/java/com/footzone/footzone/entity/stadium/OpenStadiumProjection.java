package com.footzone.footzone.entity.stadium;


import com.footzone.footzone.entity.comment.CommentProjection2;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface OpenStadiumProjection {

    UUID getStadiumId();

    String getStadiumName();

    Double getHourlyPrice();

    boolean getIsOpen();

    LocalTime getCloseTime();

    @Value("#{@commentRepository.countAllComments(target.stadiumId )}")
    List<CommentProjection2> getRate();

    @Value("#{@stadiumRepository.getAllStadiumPhotosNameByStadiumId(target.stadiumId )}")
    List<String> getPhotos();
}
