package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.DTO.*;
import com._kforbreakfast._kforbreakfast.model.Activity;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.repository.ActivityRepository;
import com._kforbreakfast._kforbreakfast.repository.DailyActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


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

    public int calculateLongestStreak() {
        List<DailyActivity> activities = dailyActivityRepository.findAllByOrderByDateDesc();
        if (activities.isEmpty()) return 0;

        int streak = 0;
        int longestStreak = 0;
        int dayActivityCount = 0;
        LocalDate currentDay = activities.getFirst().getDate();

        for (DailyActivity da : activities) {
            if (!da.getDate().equals(currentDay)) {
                if (dayActivityCount == ACTIVITIES_PER_DAY) {
                    streak++;
                }else{
                    if(streak > longestStreak){
                        longestStreak = streak;
                    }
                    streak = 0;
                }
                currentDay = da.getDate();
                dayActivityCount = 0;
            }

            if (da.getIsComplete()) {
                dayActivityCount++;
            }
            System.out.println(da.getDate());

        }
        if (dayActivityCount == ACTIVITIES_PER_DAY) {
            streak++;
        }

        return longestStreak;
    }

    public List<LocalDate> getLastSevenDaysDates(){
        LocalDate today = LocalDate.now();
        List<DailyActivity> lastSevenDays = dailyActivityRepository.findByDateBetweenOrderByDateAscActivityTitleAsc(today.minusDays(7),today.minusDays(1));
        List<LocalDate> lastSevenDaysDates = new ArrayList<>();
        for (int i = 0; i < lastSevenDays.size(); i=i+ACTIVITIES_PER_DAY) {
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


        for (int i = 0; i < ACTIVITIES_PER_DAY; i++) {
            String currentActivityName = lastSevenDays.get(i).getActivity().getTitle();
            int currentPercentage = 0;
            List<Boolean> completion = new ArrayList<>();
            for(int j = 0; j < lastSevenDays.size(); j=j+ACTIVITIES_PER_DAY) {
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

        for (int i = 0; i < lastSevenDays.size(); i=i+ACTIVITIES_PER_DAY) {
            String dayOfWeek = lastSevenDays.get(i).getDate().getDayOfWeek().toString();
            LocalDate date = lastSevenDays.get(i).getDate();
            int activitiesComplete = 0;
            for(int j = 0; j < ACTIVITIES_PER_DAY; j++) {
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

    public List<AverageCompletionByDay> getAverageCompletionByDay() {
        List<DailyActivity> dailyActivities = dailyActivityRepository.findAllByOrderByDateAsc();
        Map<String, Double> averageCompletionByDay = new HashMap<>();
        Map<String, Integer> dayCounts = new HashMap<>();
        List<String> dayOrder = Arrays.asList(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        );

        for (DayOfWeek day : DayOfWeek.values()) {
            averageCompletionByDay.put(day.toString(), 0.0);
            dayCounts.put(day.toString(), 0);
        }

        for (int i = 0; i < dailyActivities.size(); i=i+ACTIVITIES_PER_DAY) {
            double dayTotal = 0;
            for(int j = 0; j < ACTIVITIES_PER_DAY; j++) {
                dayTotal += dailyActivities.get(i+j).getIsComplete() ? 1 : 0;
            }
            String dayOfWeek = dailyActivities.get(i).getDate().getDayOfWeek().toString();
            averageCompletionByDay.put(dayOfWeek, averageCompletionByDay.get(dayOfWeek) + dayTotal);
            dayCounts.put(dayOfWeek, dayCounts.get(dayOfWeek) + 1);
        }

        List<AverageCompletionByDay> averageCompletionByDays = new ArrayList<>();
        for (String day : averageCompletionByDay.keySet()) {
            double total = averageCompletionByDay.get(day);
            int count = dayCounts.get(day);
            double averagePercentage = (count == 0) ? 0.0 : Math.round(((total / (count * ACTIVITIES_PER_DAY)) * 100) * 10.0) / 10.0;
            System.out.println(day + " -> average completion: " + averagePercentage + "%");
            averageCompletionByDays.add(new AverageCompletionByDay(day.charAt(0) +day.substring(1).toLowerCase() , averagePercentage));
        }

        averageCompletionByDays.sort(Comparator.comparingInt(d -> dayOrder.indexOf(d.dayOfWeek())));
        return averageCompletionByDays;
    }

    public List<String> getTop3CompletedActivities(){
        return calculateTopOrBottom3CompletedActivities(true);
    }

    public List<String> getBottom3CompletedActivities(){
        return calculateTopOrBottom3CompletedActivities(false);
    }

    private List<String> calculateTopOrBottom3CompletedActivities(boolean calculateTop3) {
        List<DailyActivity> dailyActivities = dailyActivityRepository.findAllByOrderByDateAsc();
        Map<String, Integer> activityCompletedCount = new HashMap<>();

        for (int i = 0; i < dailyActivities.size(); i += ACTIVITIES_PER_DAY) {
            for (int j = 0; j < ACTIVITIES_PER_DAY; j++) {
                String title = dailyActivities.get(i + j).getActivity().getTitle();
                int itemComplete = dailyActivities.get(i + j).getIsComplete() ? 1 : 0;
                activityCompletedCount.merge(title, itemComplete, Integer::sum);
            }
        }

        if (calculateTop3) {
            return activityCompletedCount.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .toList();
        }else{
            return activityCompletedCount.entrySet().stream()
                    .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .toList();
        }


    }

    public List<TotalCompletionsByActivityDTO> getTotalActivityCompletions() {
        List<DailyActivity> dailyActivities = dailyActivityRepository.findAllByOrderByDateAsc();
        Map<String, Integer> activityCompletedCount = new HashMap<>();
        LocalDate firstDate = dailyActivities.get(0).getDate();
        int index = 0;
        while(dailyActivities.get(index).getDate().equals(firstDate)){
            activityCompletedCount.put(dailyActivities.get(index).getActivity().getTitle(), 0);
            index++;
        }
        for (DailyActivity dailyActivity : dailyActivities) {
            int itemComplete = dailyActivity.getIsComplete() ? 1 : 0;
            activityCompletedCount.merge(dailyActivity.getActivity().getTitle(), itemComplete, Integer::sum);
        }
        List<TotalCompletionsByActivityDTO> totalCompletionsByActivityDTOs = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : activityCompletedCount.entrySet()) {
            String activityTitle = entry.getKey();
            Integer activityCount = entry.getValue();
            double totalDays = (double) dailyActivities.size() / ACTIVITIES_PER_DAY;
            double percentage = Math.round((activityCount / totalDays) *100 * 100) / 100.0;
            totalCompletionsByActivityDTOs.add(new TotalCompletionsByActivityDTO(activityTitle, activityCount, percentage));

        }

        totalCompletionsByActivityDTOs.sort(
                Comparator.comparingDouble(TotalCompletionsByActivityDTO::percentage).reversed()
        );
        return totalCompletionsByActivityDTOs;
    }

    public List<ActivityStatusDTO> getStatsForDay(LocalDate date) {
        List<DailyActivity> dailyActivities = dailyActivityRepository.findByDate(date);
        System.out.println(date);
        List<ActivityStatusDTO> activityStatusDTOs = new ArrayList<>();

        for (DailyActivity dailyActivity : dailyActivities) {
            activityStatusDTOs.add(new ActivityStatusDTO(dailyActivity.getActivity().getTitle(), dailyActivity.getIsComplete()));
        }
        return activityStatusDTOs;
    }
}