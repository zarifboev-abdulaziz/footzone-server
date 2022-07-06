package com.footzone.footzone.entity.date;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DateRepository extends JpaRepository<Date, UUID> {
    Optional<Date> findByLocalDate(LocalDate localDate);
    boolean existsByLocalDate(LocalDate localDate);
}
