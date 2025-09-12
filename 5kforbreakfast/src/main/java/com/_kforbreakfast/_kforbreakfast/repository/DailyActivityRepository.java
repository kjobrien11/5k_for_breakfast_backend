package com._kforbreakfast._kforbreakfast.repository;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    Optional<DailyActivity> findByDateAndActivityId(LocalDate date, Long activityId);
    List<DailyActivity> findByDate(LocalDate date);
    boolean existsByDate(LocalDate date);
    List<DailyActivity> findAllByOrderByDateDesc();
}


