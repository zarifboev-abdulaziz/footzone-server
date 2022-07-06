package com.footzone.footzone.entity.favoriteStadium;

import java.util.UUID;

public interface FavoriteStadiumProjection {

    UUID getStadiumId();

    String getNameUz();

    String getHourlyPrice();

    String getDescription();

    String getAddress();


}
