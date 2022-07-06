package com.footzone.footzone.entity.time;


import com.footzone.footzone.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;


    public HttpEntity<?> deleteTimeById(UUID id) {
        List<Time> timeList = timeRepository.findAll();
        for (Time time : timeList) {
            if (time.getId().equals(id)) {
                timeRepository.deleteById(id);
                return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
    }

    public HttpEntity<?> getTimeById(UUID id) {

        Optional<TimeProjection> optionalTime = timeRepository.findTimeById(id);
        if (!optionalTime.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NO_CONTENT);

        }
        TimeProjection time = optionalTime.get();

        return new ResponseEntity<>(new ApiResponse("success", true, time), HttpStatus.OK);


    }
    public HttpEntity<?> addTime(Time timeDto) {
        Time time = new Time();

        time.setTime(timeDto.getTime());
        timeRepository.save(time);
        return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
    }

    public HttpEntity<?> getTimeAll(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size
        );
        Page<TimeProjection> all = timeRepository.findAllTimeByPage(
                pageable);

        return ResponseEntity.ok(new ApiResponse("success", true, all));

    }

    public HttpEntity<?> editTime(Time timeDto, UUID id) {
        Optional<Time> optionalTime = timeRepository.findById(id);
        if (!optionalTime.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }
        Time time = optionalTime.get();
        time.setTime(timeDto.getTime());
        timeRepository.save(time);
        return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
    }

    public Time getOrCreate(LocalTime time){
        Optional<Time> optionalTime = timeRepository.findByTime(time);
        if (optionalTime.isPresent()) {
            return optionalTime.get();
        } else {
            Time saveTime = timeRepository.save(new Time(time));
            return saveTime;
        }
    }
}
