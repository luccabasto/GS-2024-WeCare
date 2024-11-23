package com.wecare.wecare_backend.repository;

import com.wecare.wecare_backend.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByGoalId(Long goalId);

    Object findByUserId(long l);
}
