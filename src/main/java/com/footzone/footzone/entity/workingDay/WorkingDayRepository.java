package com.footzone.footzone.entity.workingDay;

import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, UUID> {
    boolean existsByDayNameAndStartTimeAndEndTime(WeekDay dayName, Time startTime, Time endTime);
    Optional<WorkingDay> findByDayNameAndStartTimeAndEndTime(WeekDay dayName, LocalTime startTime, LocalTime endTime);
    Optional<WorkingDay> findByDayNameAndStartTimeIdAndEndTimeId(WeekDay dayName, UUID startTime_id, UUID endTime_id);

    @Query(nativeQuery = true,
            value = "select wd.day_name as dayName,\n" +
                    "       t1.time as startTime,\n" +
                    "       t2.time as endTime\n" +
                    "from stadiums s\n" +
                    "         join stadiums_working_days swd on s.id = swd.stadium_id\n" +
                    "         join working_days wd on swd.working_day_id = wd.id\n" +
                    "         join times t1 on wd.start_time_id = t1.id\n" +
                    "         join times t2 on wd.end_time_id = t2.id\n" +
                    "where s.id = :stadiumId")
    List<WorkingDayProjection2> findWorkingDaysByStadiumId(UUID stadiumId);
}
