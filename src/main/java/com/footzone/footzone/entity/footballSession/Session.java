package com.footzone.footzone.entity.footballSession;

import com.footzone.footzone.entity.date.Date;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.enums.Status;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "sessions")
public class Session extends AbsEntity {

    @ManyToOne
    private Stadium stadium;

    @ManyToOne
    private Date startDate;

    @ManyToOne
    private Time startTime;

    @ManyToOne
    private Time endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private double totalAmount;

    @ManyToOne
    private User user;
}
