package com.footzone.footzone.entity.date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@PackagePrivate
public class DateDto {
    LocalDate localDate;
}
