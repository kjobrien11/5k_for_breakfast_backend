package com._kforbreakfast._kforbreakfast.controller;

import com._kforbreakfast._kforbreakfast.model.Activity;
import com._kforbreakfast._kforbreakfast.service.ActivityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/all")
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }
}