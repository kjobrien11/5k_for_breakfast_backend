package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.DTO.*;
import com._kforbreakfast._kforbreakfast.model.Activity;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.repository.ActivityRepository;
import com._kforbreakfast._kforbreakfast.repository.DailyActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DailyActivityService {

    private final int ACTIVITIES_PER_DAY = 5;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<DailyActivityDTO> getAllActivities() {
        return dailyActivityRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<DailyActivityDTO> getTodaysActivities() {
        LocalDate today = LocalDate.now();
        return dailyActivityRepository.findByDate(today).stream()
                .map(this::toDTO)
                .toList();
    }

    private DailyActivityDTO toDTO(DailyActivity da) {
        return new DailyActivityDTO(
                da.getId(),
                da.getDate(),
                da.getIsComplete(),
                new ActivityDTO(
                        da.getActivity().getId(),
                        da.getActivity().getTitle(),
                        da.getActivity().getDescription()
                )
        );
    }

    public DailyActivityDTO markComplete(LocalDate date, Long activityId) {
        return updateCompleteStatus(date, activityId, true);
    }

    public DailyActivityDTO markUnComplete(LocalDate date, Long activityId) {
        return updateCompleteStatus(date, activityId, false);
    }

    public DailyActivityDTO updateCompleteStatus(LocalDate date, Long activityId,boolean isComplete) {
        DailyActivity da = dailyActivityRepository
                .findByDateAndActivityId(date, activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        da.setIsComplete(isComplete);
        dailyActivityRepository.save(da);

        return new DailyActivityDTO(
                da.getId(),
                da.getDate(),
                da.getIsComplete(),
                new ActivityDTO(
                        da.getActivity().getId(),
                        da.getActivity().getTitle(),
                        da.getActivity().getDescription()
                )
        );
    }

    public ProgressDTO progressToday(){
        LocalDate today = LocalDate.now();

        List<DailyActivity> todaysActivities = dailyActivityRepository
                .findByDate(today);

        int total = todaysActivities.size();
        int completed = (int) todaysActivities.stream()
                .filter(DailyActivity::getIsComplete)
                .count();

        double percentage = total == 0 ? 0 : (completed * 100.0 / total);

        return new ProgressDTO(total, completed, percentage);
    }

    public int calculateStreak() {
        List<DailyActivity> activities = dailyActivityRepository.findAllByOrderByDateDesc();
        if (activities.isEmpty()) return 0;

        int streak = 0;
        int dayActivityCount = 0;
        LocalDate currentDay = activities.getFirst().getDate();
        LocalDate today = LocalDate.now();

        for (DailyActivity da : activities) {
            if (!da.getDate().equals(currentDay)) {
                if (dayActivityCount == ACTIVITIES_PER_DAY) {
                    streak++;
                } else if(!da.getDate().equals(today.minusDays(1))) {
                    break;
                }
                currentDay = da.getDate();
                dayActivityCount = 0;
            }

            if (da.getIsComplete()) {
                dayActivityCount++;
            }
        }

        if (dayActivityCount == ACTIVITIES_PER_DAY) {
            streak++;
        }

        return streak;
    }

    public List<LocalDate> getLastSevenDaysDates(){
        LocalDate today = LocalDate.now();
        List<DailyActivity> lastSevenDays = dailyActivityRepository.findByDateBetweenOrderByDateAscActivityTitleAsc(today.minusDays(7),today.minusDays(1));
        List<LocalDate> lastSevenDaysDates = new ArrayList<>();
        for (int i = 0; i < lastSevenDays.size(); i=i+5) {
            lastSevenDaysDates.add(lastSevenDays.get(i).getDate());
        }
        return lastSevenDaysDates;
    }

    public List<SevenDayBreakdownDTO> getLastSevenDaysBreakdown(){
        LocalDate today = LocalDate.now();
        List<DailyActivity> lastSevenDays = dailyActivityRepository.findByDateBetweenOrderByDateAscActivityTitleAsc(today.minusDays(7),today.minusDays(1));
        List<SevenDayBreakdownDTO>  sevenDayBreakdown = new ArrayList<>();
        System.out.println();
        for (int i = 0; i < lastSevenDays.size(); i++) {
            System.out.println(lastSevenDays.get(i).getActivity().getTitle());
        }


        for (int i = 0; i < 5; i++) {
            String currentActivityName = lastSevenDays.get(i).getActivity().getTitle();
            int currentPercentage = 0;
            List<Boolean> completion = new ArrayList<>();
            for(int j = 0; j < lastSevenDays.size(); j=j+5) {
                if(lastSevenDays.get(j+i).getIsComplete()){
                    System.out.println(lastSevenDays.get(j+i).getActivity().getTitle());
                    currentPercentage++;
                    completion.add(true);
                }else{
                    completion.add(false);
                }
            }
            currentPercentage = (int) ((currentPercentage / 7.0) * 100);
            sevenDayBreakdown.add(new SevenDayBreakdownDTO(currentActivityName, currentPercentage, completion));

        }

        return sevenDayBreakdown;
    }


    public List<WeekHistoryDTO> getLastNDaysActivities(int days){
        LocalDate today = LocalDate.now();
        List<DailyActivity> lastSevenDays = dailyActivityRepository.findByDateBetweenOrderByDateAscActivityTitleAsc(today.minusDays(days),today.minusDays(1));
        List<WeekHistoryDTO>  daysStats = new ArrayList<>();

        for (int i = 0; i < lastSevenDays.size(); i=i+5) {
            String dayOfWeek = lastSevenDays.get(i).getDate().getDayOfWeek().toString();
            LocalDate date = lastSevenDays.get(i).getDate();
            int activitiesComplete = 0;
            for(int j = 0; j < 5; j++) {
                if(lastSevenDays.get(j+i).getIsComplete()){
                    activitiesComplete++;
                }
            }
            activitiesComplete = activitiesComplete *20;
            daysStats.add(new WeekHistoryDTO(dayOfWeek, date, activitiesComplete));
        }

        return daysStats;
    }

    public DailyActivityDTO updateActivity(LocalDate date, Long activityId, boolean complete) {
        Optional<DailyActivity> activityToUpdate = dailyActivityRepository.findByDateAndActivityId(date, activityId);

        if(activityToUpdate.isPresent()) {
            activityToUpdate.get().setIsComplete(complete);
            dailyActivityRepository.save(activityToUpdate.get());
            return toDTO(activityToUpdate.get());
        }
        System.out.println("COULD NO FIND");
        return null;
    }


    public void createActivitiesForDateIfMissing(LocalDate date) {
        boolean exists = dailyActivityRepository.existsByDate(date);
        if (!exists) {
            List<Activity> allActivities = activityRepository.findAll();
            for (Activity activity : allActivities) {
                DailyActivity da = new DailyActivity();
                da.setDate(date);
                da.setActivity(activity);
                da.setIsComplete(false);
                dailyActivityRepository.save(da);
            }
        }
    }
}