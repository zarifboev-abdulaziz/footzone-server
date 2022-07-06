package com.footzone.footzone.entity.device;

import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "devices")
public class Device extends AbsEntity {

    @Column()
    private String name;

    @Column(columnDefinition = " varchar(100) default 'MOBILE' ")
    private String type;

    @Column()
    private String deviceToken;

    @ManyToOne
    private User user;

}
