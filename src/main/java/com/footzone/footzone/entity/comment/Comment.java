package com.footzone.footzone.entity.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "comments")
public class Comment extends AbsEntity {

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private byte rate;

    @JsonBackReference
    @ManyToOne
    private Stadium stadium;




}
