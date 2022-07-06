package com.footzone.footzone.entity.attachment;

import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "attachments")
public class Attachment extends AbsEntity {

    @Column
    private String originalName;

    @Column
    private float size;

    @Column
    private String contentType;

    @Column
    private String name;

}
