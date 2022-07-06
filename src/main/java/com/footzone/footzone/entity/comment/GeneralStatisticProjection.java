package com.footzone.footzone.entity.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralStatisticProjection {

    List<CommentProjection2> commentInfo;

    List<CommentProjection> allComments;

}
