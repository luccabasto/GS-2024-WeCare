package com.wecare.wecare_backend.controller;

import com.wecare.wecare_backend.dto.GoalDTO;
import com.wecare.wecare_backend.service.GoalService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")

public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public EntityModel<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) {
        GoalDTO savedGoal = goalService.saveGoal(goalDTO);
        return EntityModel.of(savedGoal,
                linkTo(methodOn(GoalController.class).getGoal(savedGoal.getId())).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<GoalDTO> getGoal(@PathVariable Long id) {
        GoalDTO goal = goalService.getGoalById(id);
        return EntityModel.of(goal,
                linkTo(methodOn(GoalController.class).getAllGoals()).withRel("all-goals"));
    }

    @GetMapping
    public CollectionModel<GoalDTO> getAllGoals() {
        List<GoalDTO> goals = goalService.getAllGoals();
        return CollectionModel.of(goals,
                linkTo(methodOn(GoalController.class).getAllGoals()).withSelfRel());
    }

    @GetMapping("/user/{userId}")
    public CollectionModel<GoalDTO> getGoalsByUser(@PathVariable Long userId) {
        List<GoalDTO> goals = goalService.getGoalsByUserId(userId);
        return CollectionModel.of(goals,
                linkTo(methodOn(GoalController.class).getGoalsByUser(userId)).withSelfRel());
    }
}
