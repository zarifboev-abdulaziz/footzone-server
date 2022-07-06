package com.footzone.footzone.entity.time;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface TimeRepository extends JpaRepository<Time, UUID> {


    @Query(nativeQuery = true, value = "select cast( t.id as varchar ) as timeId, t.time as time\n" +
            "from times t")
    Page<TimeProjection> findAllTimeByPage(Pageable pageable);

    @Query(nativeQuery = true, value = "select cast( t.id as varchar ) as timeId, t.time as time\n" +
            "from times t where t.id =:id")
    Optional<TimeProjection> findTimeById(UUID id);

    Optional<Time> findByTime(LocalTime time);

}
