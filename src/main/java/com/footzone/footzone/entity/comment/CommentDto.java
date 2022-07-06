package com.footzone.footzone.entity.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    private String text;

    private byte rate;

    private UUID stadiumId;
}
