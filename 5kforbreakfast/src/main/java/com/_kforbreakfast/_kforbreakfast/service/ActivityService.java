package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.model.Activity;
import com._kforbreakfast._kforbreakfast.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }
}
