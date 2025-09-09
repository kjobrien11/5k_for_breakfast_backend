package com._kforbreakfast._kforbreakfast.controller;

import com._kforbreakfast._kforbreakfast.DTO.DailyActivityDTO;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.service.DailyActivityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private DailyActivityService activityService;

    @GetMapping("/all")
    public List<DailyActivityDTO> getAllActivities() {
        return activityService.getAllActivities();

    }
}