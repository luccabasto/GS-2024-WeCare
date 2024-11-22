package com.wecare.wecare_backend.service;

import com.wecare.wecare_backend.dto.HabitDTO;
import com.wecare.wecare_backend.model.Habit;
import com.wecare.wecare_backend.repository.HabitRepository;
import com.wecare.wecare_backend.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private GoalRepository goalRepository = null;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
        this.goalRepository = goalRepository;
    }

    public HabitDTO saveHabit(HabitDTO habitDTO) {
        Habit habit = new Habit();
        habit.setNome(habitDTO.getName());
        habit.setGoal(goalRepository.findById(habitDTO.getGoalId())
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada")));
        habit = habitRepository.save(habit);
        return convertToDTO(habit);
    }

    public HabitDTO getHabitById(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hábito não encontrado"));
        return convertToDTO(habit);
    }

    public List<HabitDTO> getAllHabits() {
        return habitRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HabitDTO> getHabitsByGoalId(Long goalId) {
        return habitRepository.findByGoalId(goalId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HabitDTO convertToDTO(Habit habit) {
        HabitDTO dto = new HabitDTO();
        dto.setId(habit.getId());
        dto.setName(habit.getNome());
        dto.setGoalId(habit.getGoal().getId());
        dto.setCreatedAt(habit.getCriado_em().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }

}
