package com.footzone.footzone.entity.date;

import com.footzone.footzone.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DateService {

    private final DateRepository dateRepository;

    public ResponseEntity<?> getAllDates() {
        List<Date> dates = dateRepository.findAll();

        ApiResponse dateList = ApiResponse.builder().data(dates).message("Date list").success(true).build();

        ApiResponse emptyList = ApiResponse.builder().data(dates).message("List is empty").success(false).build();

        if (dates.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(emptyList);
        return ResponseEntity.ok(dateList);
    }


    public ResponseEntity<?> getDateById(UUID dateID) {
        Optional<Date> optionalDate = dateRepository.findById(dateID);

        if (optionalDate.isPresent()) {
            ApiResponse date = ApiResponse.builder().data(optionalDate.get()).message("Date by id").success(true).build();
            return ResponseEntity.ok(date);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> addDate(DateDto dateDto) {
        Date newDate = new Date(dateDto.localDate);
        Date savedDate = dateRepository.save(newDate);
        ApiResponse build = ApiResponse.builder().data(savedDate).message("Date is saved !").success(true).build();
        return ResponseEntity.ok(build);
    }


    public ResponseEntity<?> editDate(UUID dateId, DateDto dateDto) {
        Optional<Date> optionalDate = dateRepository.findById(dateId);
        if (optionalDate.isPresent()){
            Date editingDate = optionalDate.get();
            editingDate.setLocalDate(dateDto.getLocalDate());
            Date savedDate = dateRepository.save(editingDate);
            ApiResponse editedDate = ApiResponse.builder().data(savedDate).message("edited date").success(true).build();
            return ResponseEntity.ok(editedDate);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> deleteDate(UUID dateId) {
        try {
            dateRepository.deleteById(dateId);
            return ResponseEntity.ok("deleted");
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
