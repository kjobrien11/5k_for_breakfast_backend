package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.DTO.ActivityDTO;
import com._kforbreakfast._kforbreakfast.DTO.DailyActivityDTO;
import com._kforbreakfast._kforbreakfast.DTO.ProgressDTO;
import com._kforbreakfast._kforbreakfast.model.Activity;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.repository.ActivityRepository;
import com._kforbreakfast._kforbreakfast.repository.DailyActivityRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;


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