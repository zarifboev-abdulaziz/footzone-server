package com.footzone.footzone.entity.favoriteStadium;

import com.footzone.footzone.entity.stadium.StadiumProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteStadiumRepository extends JpaRepository<FavoriteStadium, UUID> {

    boolean existsByCreatedByIdAndStadiumId(UUID createdBy_id, UUID stadium_id);

    FavoriteStadium findByCreatedByIdAndStadiumId(UUID createdBy_id, UUID stadium_id);


/*    @Query(nativeQuery = true, value = "select cast(s.id as varchar )        as stadiumId,\n" +
            "       s.name_uz                                                           as nameUz,\n" +
            "       s.hourly_price                                                      as hourlyPrice,\n" +
            "       s.description                                                       as description,\n" +
            "      cast( array_agg(r.name_uz || ' ' || d.name_uz || ' ' || a.street_name_uz) as varchar ) as address\n" +
            "\n" +
            "from favorite_stadium\n" +
            "         join stadiums s on s.id = favorite_stadium.stadium_id\n" +
            "         join addresses a on a.id = s.address_id\n" +
            "         join districts d on d.id = a.district_id\n" +
            "         join regions r on d.region_id = r.id\n" +
            "where lower(s.name_uz) like lower(concat('%', :search, '%'))\n" +
            "   or lower(s.name_ru) like lower(concat('%', :search, '%'))\n" +
            "   or lower(s.name_en) like lower(concat('%', :search, '%'))\n" +
            "group by s.id")
    Page<FavoriteStadiumProjection> getFavoriteStadiumByPages(Pageable pageable, String search);*/


    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from favorite_stadium fs\n" +
                    "         join stadiums s on s.id = fs.stadium_id\n" +
                    "where fs.created_by_id = :userId\n" +
                    "and s.is_deleted = false")
    List<StadiumProjection> findStadiumIdByUserId(UUID userId);


    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)\n" +
                    "from favorite_stadium fs\n" +
                    "         join stadiums s on fs.stadium_id = s.id\n" +
                    "where fs.created_by_id = :userId\n" +
                    "  and s.is_deleted = false")
    List<String> getIdsByUserId(UUID userId);
}
