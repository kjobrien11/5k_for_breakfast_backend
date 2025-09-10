package com._kforbreakfast._kforbreakfast.controller;

import com._kforbreakfast._kforbreakfast.DTO.CompleteActivityRequest;
import com._kforbreakfast._kforbreakfast.DTO.DailyActivityDTO;
import com._kforbreakfast._kforbreakfast.DTO.ProgressDTO;
import com._kforbreakfast._kforbreakfast.service.DailyActivityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;

    @GetMapping("/all")
    public List<DailyActivityDTO> getAllActivities() {
        return dailyActivityService.getAllActivities();
    }

    @GetMapping("/today")
    public List<DailyActivityDTO> getTodaysActivities() {
        return dailyActivityService.getTodaysActivities();
    }

    @GetMapping("/today/progress")
    public ProgressDTO progressToday() {
        return dailyActivityService.progressToday();
    }

    @PostMapping("/complete")
    public DailyActivityDTO markComplete(@RequestBody CompleteActivityRequest request){
        return dailyActivityService.markComplete(request.date(), request.activityId());
    }

    @PostMapping("/uncomplete")
    public DailyActivityDTO markUnComplete(@RequestBody CompleteActivityRequest request){
        return dailyActivityService.markUnComplete(request.date(), request.activityId());
    }
}