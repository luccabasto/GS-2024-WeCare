package com.wecare.wecare_backend.repository;

import com.wecare.wecare_backend.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository  extends JpaRepository<Goal, Long>{

    List<Goal> findByUserId(Long userId);

}
