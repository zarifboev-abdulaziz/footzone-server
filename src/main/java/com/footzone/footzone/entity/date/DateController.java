package com.footzone.footzone.entity.date;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.domain}" + "/date")
public class DateController {

    private final DateService dateService;

    @GetMapping
    public ResponseEntity<?> getAllDates() {
        return dateService.getAllDates();
    }

    @GetMapping("/{dateID}")
    public ResponseEntity<?> getDateById(@PathVariable UUID dateID) {
        return dateService.getDateById(dateID);
    }

    @PostMapping
    public ResponseEntity<?> addDate(@RequestBody DateDto dateDto) {
        return dateService.addDate(dateDto);
    }

    @PutMapping("/{dateId}")
    public ResponseEntity<?> editDate(@PathVariable UUID dateId, @RequestBody DateDto dateDto) {
        return dateService.editDate(dateId, dateDto);
    }

    @DeleteMapping("/{dateId}")
    public ResponseEntity<?> deleteDate(@PathVariable UUID dateId) {
        return dateService.deleteDate(dateId);
    }
}
