package com._kforbreakfast._kforbreakfast.repository;
import com._kforbreakfast._kforbreakfast.model.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
}
