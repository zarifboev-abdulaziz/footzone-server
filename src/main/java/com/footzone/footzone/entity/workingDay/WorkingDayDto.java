package com.footzone.footzone.entity.workingDay;

import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.enums.WeekDay;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkingDayDto {

    @Enumerated(EnumType.STRING)
    private WeekDay dayName;

    @ManyToOne
    private LocalTime startTime;

    @ManyToOne
    private LocalTime endTime;

}
