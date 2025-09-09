package com._kforbreakfast._kforbreakfast.DTO;

import java.time.LocalDate;

public record CompleteActivityRequest(
        LocalDate date,
        Long activityId) {
}
