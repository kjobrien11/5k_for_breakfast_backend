package com._kforbreakfast._kforbreakfast.service;

import com._kforbreakfast._kforbreakfast.DTO.ActivityDTO;
import com._kforbreakfast._kforbreakfast.DTO.DailyActivityDTO;
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
                .map(da -> new DailyActivityDTO(
                        da.getId(),
                        da.getDate(),
                        da.getIsComplete(),
                        new ActivityDTO(
                                da.getActivity().getId(),
                                da.getActivity().getTitle(),
                                da.getActivity().getDescription()
                        )
                ))
                .toList();
    }
}