package com.footzone.footzone.entity.footballSession;
//Asliddin Kenjaev, created: May, 27 2022 5:07 PM

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByStadiumId(UUID stadium_id);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)  as id,\n" +
                    "       s2.name                as stadiumName,\n" +
                    "       cast(s2.id as varchar) as stadiumId,\n" +
                    "       d.local_date           as startDate,\n" +
                    "       t1.time                as startTime,\n" +
                    "       t2.time                as endTime,\n" +
                    "       s.status               as status,\n" +
                    "       s2.hourly_price        as hourlyPrice,\n" +
                    "       s2.longitude           as longitude,\n" +
                    "       s2.latitude            as latitude\n" +
                    "from sessions s\n" +
                    "         join stadiums s2 on s.stadium_id = s2.id\n" +
                    "         join dates d on s.start_date_id = d.id\n" +
                    "         join times t1 on s.start_time_id = t1.id\n" +
                    "         join times t2 on s.end_time_id = t2.id\n" +
                    "where cast(s.status as varchar) = :sessionStatus\n" +
                    "  and s2.owner_id = :ownerId\n" +
                    "  and s2.is_deleted = false\n" +
                    "order by s.created_at")
    List<SessionProjection> getPendingSessionsForOwner(String sessionStatus, UUID ownerId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)  as id,\n" +
                    "       s2.name                as stadiumName,\n" +
                    "       cast(s2.id as varchar) as stadiumId,\n" +
                    "       d.local_date           as startDate,\n" +
                    "       t1.time                as startTime,\n" +
                    "       t2.time                as endTime,\n" +
                    "       s.status               as status,\n" +
                    "       s2.hourly_price        as hourlyPrice,\n" +
                    "       s2.longitude           as longitude,\n" +
                    "       s2.latitude            as latitude\n" +
                    "from sessions s\n" +
                    "         join stadiums s2 on s.stadium_id = s2.id\n" +
                    "         join dates d on s.start_date_id = d.id\n" +
                    "         join times t1 on s.start_time_id = t1.id\n" +
                    "         join times t2 on s.end_time_id = t2.id\n" +
                    "where cast(s.status as varchar) = :sessionStatus\n" +
                    "  and s2.owner_id = :ownerId\n" +
                    "  and s2.is_deleted = false\n" +
                    "order by s.created_at desc")
    List<SessionProjection> getPlayedSessionsForOwner(String sessionStatus, UUID ownerId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)  as id,\n" +
                    "       s2.name                as stadiumName,\n" +
                    "       cast(s2.id as varchar) as stadiumId,\n" +
                    "       d.local_date           as startDate,\n" +
                    "       t1.time                as startTime,\n" +
                    "       t2.time                as endTime,\n" +
                    "       s.status               as status,\n" +
                    "       s2.hourly_price        as hourlyPrice,\n" +
                    "       s2.longitude           as longitude,\n" +
                    "       s2.latitude            as latitude\n" +
                    "from sessions s\n" +
                    "         join stadiums s2 on s.stadium_id = s2.id\n" +
                    "         join dates d on s.start_date_id = d.id\n" +
                    "         join times t1 on s.start_time_id = t1.id\n" +
                    "         join times t2 on s.end_time_id = t2.id\n" +
                    "where (cast(s.status as varchar) = 'ACCEPTED' or\n" +
                    "       cast(s.status as varchar) = 'PENDING' or\n" +
                    "       cast(s.status as varchar) = 'DECLINED')\n" +
                    "  and s2.owner_id = :ownerId\n" +
                    "  and s2.is_deleted = false\n" +
                    "order by s.status desc")
    List<SessionProjection> getNotificationSessionsForOwner(UUID ownerId);


    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)            as id,\n" +
                    "       cast(s.stadium_id as varchar)    as stadiumId,\n" +
                    "       cast(d.local_date as varchar)    as startDate,\n" +
                    "       cast(t1.time as varchar)         as startTime,\n" +
                    "       cast(t2.time as varchar)         as endTime,\n" +
                    "       s.status                         as status,\n" +
                    "       s2.name                          as stadiumName,\n" +
                    "       s2.hourly_price                  as hourlyPrice\n" +
                    "from sessions s\n" +
                    "         join stadiums s2 on s.stadium_id = s2.id\n" +
                    "         join dates d on s.start_date_id = d.id\n" +
                    "         join times t1 on s.start_time_id = t1.id\n" +
                    "         join times t2 on s.end_time_id = t2.id\n" +
                    "where (status = 'PLAYED' or status = 'NOT_PLAYED')\n" +
                    "  and s.created_by_id = :createdBy_id")
    List<SessionProjection> findPlayedNotPlayedByCreatedById(UUID createdBy_id);

    @Query(value = "select s.*\n" +
            "    from sessions s\n" +
            "    where start_date_id =:startDateId \n" +
            "    and stadium_id =:stadiumId \n" +
            "    and s.status in ('ACCEPTED', 'PENDING')", nativeQuery = true)
    List<Session> getSessionsForSpecificDay(UUID startDateId, UUID stadiumId);

    @Query(nativeQuery = true,
            value = "select cast(s.id as varchar)         as id,\n" +
                    "       cast(s.stadium_id as varchar) as stadiumId,\n" +
                    "       cast(d.local_date as varchar) as startDate,\n" +
                    "       cast(t1.time as varchar)      as startTime,\n" +
                    "       cast(t2.time as varchar)      as endTime,\n" +
                    "       s.status                      as status,\n" +
                    "       st.name                       as stadiumName,\n" +
                    "       st.hourly_price               as hourlyPrice,\n" +
                    "       st.longitude                  as longitude,\n" +
                    "       st.latitude                   as latitude\n" +
                    "from sessions s\n" +
                    "         join stadiums st on s.stadium_id = st.id\n" +
                    "         join dates d on s.start_date_id = d.id\n" +
                    "         join times t1 on s.start_time_id = t1.id\n" +
                    "         join times t2 on s.end_time_id = t2.id\n" +
                    "where (status = 'ACCEPTED' or status = 'PENDING')\n" +
                    "  and d.local_date >= now()\n" +
                    "  and s.created_by_id = :user_id")
    List<SessionProjection> getSessionsOfStadiumsPlayingSoon(UUID user_id);

    @Query(nativeQuery = true,
            value = "select count(s2.id)\n" +
                    "from sessions s2\n" +
                    "         join stadiums s on s2.stadium_id = s.id\n" +
                    "         join dates d on s2.start_date_id = d.id\n" +
                    "         join times t1 on s2.start_time_id = t1.id\n" +
                    "         join times t2 on s2.end_time_id = t2.id\n" +
                    "where s2.user_id = :userId\n" +
                    "  and (status = 'ACCEPTED' or status = 'DECLINED')\n" +
                    "  and d.local_date >= now()\n" +
                    "  and t1.time > localtime + '05:00:00'")
    int hasNotificationForUser(UUID userId);

    @Query(nativeQuery = true,
            value = "select count(s.id)\n" +
                    "from sessions s\n" +
                    "         join stadiums s2 on s.stadium_id = s2.id\n" +
                    "where s2.owner_id = :ownerId\n" +
                    "  and s.status = 'PENDING'")
    int hasNotificationForAdmin(UUID ownerId);

}
