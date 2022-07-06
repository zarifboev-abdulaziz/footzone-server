package com.footzone.footzone.entity.stadium;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StadiumRepository extends JpaRepository<Stadium, UUID> {

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from stadiums s\n" +
                    "where lower(s.name) like lower(concat('%', :search, '%'))\n" +
                    "and s.is_deleted = false")
    List<StadiumProjection3> getAllStadium(String search);

    void deleteByCreatedById(UUID createdBy_id);

    @Query(value = "select cast(s.id as varchar) as stadiumId,\n" +
            "       s.name                as stadiumName,\n" +
            "       s.hourly_price        as hourlyPrice,\n" +
            "       true                  as isOpen,\n" +
            "       t2.time               as closeTime\n" +
            "from stadiums s\n" +
            "         join stadiums_working_days swd on s.id = swd.stadium_id\n" +
            "         join working_days wd on swd.working_day_id = wd.id\n" +
            "         join times t1 on wd.start_time_id = t1.id\n" +
            "         join times t2 on wd.end_time_id = t2.id\n" +
            "where s.is_active = true\n" +
            "  and trim(to_char(current_timestamp, 'DAY')) = wd.day_name\n" +
            "  and t1.time < localtime + '05:00:00'\n" +
            "  and t2.time > localtime + '05:00:00'\n" +
            "  and s.is_deleted = false",
            nativeQuery = true)
    List<OpenStadiumProjection> getOpenStadiums();

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from stadiums s\n" +
                    "where s.owner_id = :ownerId\n" +
                    "and s.is_deleted = false")
    List<StadiumProjection> findAllStadiumByOwnerId(UUID ownerId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)           as stadiumId,\n" +
                    "       s.name         as name,\n" +
                    "       s.hourly_price as hourlyPrice,\n" +
                    "       s.latitude     as latitude,\n" +
                    "       s.longitude    as longitude,\n" +
                    "       s.is_active    as isActive\n" +
                    "from stadiums s\n" +
                    "where calculate_distance(:userLat, :userLong, s.latitude, s.longitude) < 40000\n" +
                    "and s.is_deleted = false")
    List<StadiumProjection> findNearStadium(double userLat, double userLong);

    @Query(nativeQuery = true, value = "select a.name from stadiums s\n" +
            "join stadiums_photos sp on s.id = sp.stadium_id\n" +
            "join attachments a on a.id = sp.photo_id\n" +
            "where s.id =:stadiumId\n" +
            "and s.is_deleted = false")
    List<String> getAllStadiumPhotosNameByStadiumId(UUID stadiumId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as stadiumName,\n" +
                    "       s.number              as number,\n" +
                    "       s.address             as address,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.latitude            as latitude\n" +
                    "from stadiums s\n" +
                    "where s.id = :stadiumId\n" +
                    "and s.is_deleted = false")
    Optional<StadiumProjection2> findStadiumById(UUID stadiumId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from stadiums s\n" +
                    "where s.id = :stadiumId\n" +
                    "and s.is_deleted = false")
    Optional<StadiumProjection> findStadiumByIdBrief(UUID stadiumId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from stadiums s\n" +
                    "where lower(s.name) like lower(concat('%', :search, '%'))\n" +
                    "and s.is_deleted = false")
    List<StadiumProjection> searchByName(String search);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar) as stadiumId,\n" +
                    "       s.name                as name,\n" +
                    "       s.hourly_price        as hourlyPrice,\n" +
                    "       s.latitude            as latitude,\n" +
                    "       s.longitude           as longitude,\n" +
                    "       s.is_active           as isActive\n" +
                    "from stadiums s\n" +
                    "         join sessions s2 on s.id = s2.stadium_id\n" +
                    "where (s2.status = 'PLAYED' or s2.status = 'NOT_PLAYED')\n" +
                    "  and s2.created_by_id = :createdBy_id")
    List<StadiumProjection> getPlayedStadiums(UUID createdBy_id);
}
