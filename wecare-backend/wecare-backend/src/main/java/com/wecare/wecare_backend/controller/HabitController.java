package com.wecare.wecare_backend.controller;

import com.wecare.wecare_backend.dto.HabitDTO;
import com.wecare.wecare_backend.service.HabitService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public EntityModel<HabitDTO> createHabit(@RequestBody HabitDTO habitDTO) {
        HabitDTO savedHabit = habitService.saveHabit(habitDTO);
        return EntityModel.of(savedHabit,
                linkTo(methodOn(HabitController.class).getHabit(savedHabit.getId())).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<HabitDTO> getHabit(@PathVariable Long id) {
        HabitDTO habit = habitService.getHabitById(id);
        return EntityModel.of(habit,
                linkTo(methodOn(HabitController.class).getAllHabits()).withRel("all-habits"));
    }

    @GetMapping
    public CollectionModel<HabitDTO> getAllHabits() {
        List<HabitDTO> habits = habitService.getAllHabits();
        return CollectionModel.of(habits,
                linkTo(methodOn(HabitController.class).getAllHabits()).withSelfRel());
    }

    @GetMapping("/goal/{goalId}")
    public CollectionModel<HabitDTO> getHabitsByGoal(@PathVariable Long goalId) {
        List<HabitDTO> habits = habitService.getHabitsByGoalId(goalId);
        return CollectionModel.of(habits,
                linkTo(methodOn(HabitController.class).getHabitsByGoal(goalId)).withSelfRel());
    }
}
