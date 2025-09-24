package com._kforbreakfast._kforbreakfast.DTO;

import java.time.LocalDate;

public record UpdateActivityDTO(LocalDate date, Long activityId, boolean complete) {
}
