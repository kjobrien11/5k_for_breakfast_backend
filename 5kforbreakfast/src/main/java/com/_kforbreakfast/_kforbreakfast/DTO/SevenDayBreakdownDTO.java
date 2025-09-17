package com._kforbreakfast._kforbreakfast.DTO;

import java.util.List;

public record SevenDayBreakdownDTO(String activityName, int percentage, List<Boolean> completion) {
}
