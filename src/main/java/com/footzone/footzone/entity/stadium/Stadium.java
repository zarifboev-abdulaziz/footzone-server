package com.footzone.footzone.entity.stadium;

import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.workingDay.WorkingDay;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "stadiums")
@Table(indexes = @Index(columnList = "name"))
public class Stadium extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private double hourlyPrice;

    private String address;

    @Column(columnDefinition = " boolean default true")
    private boolean isActive;

    @OneToMany
    @JoinTable(name = "stadiums_photos",
            joinColumns = {@JoinColumn(name = "stadium_id")},
            inverseJoinColumns = {@JoinColumn(name = "photo_id")})
    private List<Attachment> photos;

    @Column(columnDefinition = " boolean default false")
    private boolean isDeleted;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @ManyToOne
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "stadiums_working_days",
            joinColumns = @JoinColumn(name = "stadium_id"),
            inverseJoinColumns = @JoinColumn(name = "working_day_id"))
    List<WorkingDay> workingDay;

}
