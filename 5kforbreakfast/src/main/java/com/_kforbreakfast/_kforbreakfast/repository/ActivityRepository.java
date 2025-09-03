package com._kforbreakfast._kforbreakfast.repository;
import com._kforbreakfast._kforbreakfast.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
