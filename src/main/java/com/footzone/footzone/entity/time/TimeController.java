package com.footzone.footzone.entity.time;


import com.footzone.footzone.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.domain}" + "/time")
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;

    @GetMapping("/{id}")
    public HttpEntity<?> getTimeById(@PathVariable UUID id) {
        return timeService.getTimeById(id);

    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteTimeById(@PathVariable UUID id) {
        return timeService.deleteTimeById(id);

    }


    @GetMapping()
    public HttpEntity<?> getTimeAll(
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        return timeService.getTimeAll(
                page,
                size
        );

    }

    @PostMapping()
    public HttpEntity<?> addTime(@RequestBody Time timeDto) {
        return timeService.addTime(timeDto);

    }

    @PutMapping("/{id}")
    public HttpEntity<?> editTime(@RequestBody Time timeDto, @PathVariable UUID id) {
        return timeService.editTime(timeDto, id);

    }
}
