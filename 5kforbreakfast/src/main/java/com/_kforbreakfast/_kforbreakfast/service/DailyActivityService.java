package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.DTO.ActivityDTO;
import com._kforbreakfast._kforbreakfast.DTO.DailyActivityDTO;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import com._kforbreakfast._kforbreakfast.repository.DailyActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;


@Service
public class DailyActivityService {

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

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
}