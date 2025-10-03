package com._kforbreakfast._kforbreakfast.schedular;

import com._kforbreakfast._kforbreakfast.service.DailyActivityService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DailyActivityScheduler {

    private final DailyActivityService dailyActivityService;

    public DailyActivityScheduler(DailyActivityService dailyActivityService) {
        this.dailyActivityService = dailyActivityService;
    }

    @Scheduled(cron = "0 24 8 * * ?")
    public void generateDailyActivities() {
        LocalDate today = LocalDate.now();
        dailyActivityService.createActivitiesForDateIfMissing(today);
        System.out.println("Daily activities generated for " + today);
    }
}