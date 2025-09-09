package com._kforbreakfast._kforbreakfast.DTO;

import com._kforbreakfast._kforbreakfast.service.DailyActivityService;

import java.time.LocalDate;

public record DailyActivityDTO(
        Long id,
        LocalDate date,
        Boolean isComplete,
        ActivityDTO activity
) {}
