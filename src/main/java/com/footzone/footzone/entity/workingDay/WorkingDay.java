package com.footzone.footzone.entity.workingDay;

import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.enums.WeekDay;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "working_days")
public class WorkingDay extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private WeekDay dayName;

    //  In CascadeType.ALL, There is an error while creating initial data, So I changed to CascadeType.MERGE, Bahodir aka 😁
    @ManyToOne(cascade = CascadeType.MERGE)
    private Time startTime;

    //  In CascadeType.ALL, There is an error while creating initial data, So I changed to CascadeType.MERGE, Bahodir aka 😁
    @ManyToOne(cascade = CascadeType.MERGE)
    private Time endTime;

}
