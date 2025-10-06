package com._kforbreakfast._kforbreakfast.controller;

import com._kforbreakfast._kforbreakfast.DTO.*;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.service.DailyActivityService;
import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/last-seven-days")
    public List<WeekHistoryDTO> getLastSevenDaysActivities() {
        return dailyActivityService.getLastNDaysActivities(7);
    }

    @GetMapping("/last-28-days")
    public List<WeekHistoryDTO> getLast28DaysBreakdown() {
        return dailyActivityService.getLastNDaysActivities(28);
    }

    @GetMapping("/last-seven-days-dates")
    public List<LocalDate> getLastSevenDaysDates() {
        return dailyActivityService.getLastSevenDaysDates();
    }

    @GetMapping("/last-seven-days-breakdown")
    public List<SevenDayBreakdownDTO> getLastSevenDaysBreakdown() {
        return dailyActivityService.getLastSevenDaysBreakdown();
    }



    @GetMapping("/today/progress")
    public ProgressDTO progressToday() {
        return dailyActivityService.progressToday();
    }

    @PostMapping("/today/complete")
    public DailyActivityDTO markComplete(@RequestBody CompleteActivityRequest request){
        return dailyActivityService.markComplete(request.date(), request.activityId());
    }

    @PostMapping("/today/uncomplete")
    public DailyActivityDTO markUnComplete(@RequestBody CompleteActivityRequest request){
        return dailyActivityService.markUnComplete(request.date(), request.activityId());
    }

    @PostMapping("/update")
    public DailyActivityDTO updateActivity(@RequestBody UpdateActivityDTO request){
        return dailyActivityService.updateActivity(request.date(), request.activityId(), request.complete());
    }

    @GetMapping("/streak")
    public int getStreak(){
        return  dailyActivityService.calculateStreak();
    }

    @GetMapping("/longest-streak")
    public int getLongestStreak(){
        return  dailyActivityService.calculateLongestStreak();
    }

    @GetMapping("/average-completion-by-day")
    public List<AverageCompletionByDay> getAverageCompletionByDay(){
        return dailyActivityService.getAverageCompletionByDay();
    }

    @GetMapping("/top-3-completed")
    public List<String> getTop3CompletedActivities(){
        return dailyActivityService.getTop3CompletedActivities();
    }

    @GetMapping("/bottom-3-completed")
    public List<String> getBottom3CompletedActivities(){
        return dailyActivityService.getBottom3CompletedActivities();
    }
}