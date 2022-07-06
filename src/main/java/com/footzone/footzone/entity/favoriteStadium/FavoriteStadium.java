package com.footzone.footzone.entity.favoriteStadium;

import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "favoriteStadium")
public class FavoriteStadium extends AbsEntity {
    @ManyToOne
    private Stadium stadium;
}
