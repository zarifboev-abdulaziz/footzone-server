package com.footzone.footzone.entity.workingDay;

import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.entity.time.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkingDayService {
    private final WorkingDayRepository workingDayRepository;
    private final TimeService timeService;

    public List<WorkingDay> getWorkingDays(List<WorkingDayDto> days){
        List<WorkingDay> workingDays = new ArrayList<>();
        for (WorkingDayDto day : days) {
            Time startTime = timeService.getOrCreate(day.getStartTime());
            Time endTime = timeService.getOrCreate(day.getEndTime());
            Optional<WorkingDay> optionalWorkingDay = workingDayRepository.findByDayNameAndStartTimeIdAndEndTimeId(day.getDayName(),startTime.getId(),endTime.getId() );
            if (optionalWorkingDay.isPresent()) {
                workingDays.add(optionalWorkingDay.get());
            } else {
                 workingDays.add(save(new WorkingDay(day.getDayName(),startTime,endTime)));
            }
        }
        return workingDays;
    }

    public WorkingDay save(WorkingDay day){
        WorkingDay save = workingDayRepository.save(day);
        return save;
    }





}
