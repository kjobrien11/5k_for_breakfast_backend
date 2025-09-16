package com._kforbreakfast._kforbreakfast.DTO;

import java.time.LocalDate;

public record WeekHistoryDTO (String dayOfWeek, LocalDate date, int percentage){
}
