package com.footzone.footzone.entity.comment;


import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface OpenStadiumProjection2 {

    UUID getStadiumId();

    int getAllRate();

    double getAvgRate();

    int getFiveCount();

    int getFourCount();

    int getThreeCount();

    int getTwoCount();

    int getOneCount();
}
