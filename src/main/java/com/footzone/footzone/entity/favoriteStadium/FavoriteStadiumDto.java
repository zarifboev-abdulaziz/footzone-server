package com.footzone.footzone.entity.favoriteStadium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteStadiumDto {

    private UUID userId;
    private UUID stadiumId;

}
