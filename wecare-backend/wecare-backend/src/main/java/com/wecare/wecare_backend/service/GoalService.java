package com.wecare.wecare_backend.service;

import com.wecare.wecare_backend.dto.GoalDTO;
import com.wecare.wecare_backend.model.Goal;
import com.wecare.wecare_backend.repository.GoalRepository;
import com.wecare.wecare_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public GoalDTO saveGoal(GoalDTO goalDTO) {
        Goal goal = new Goal();
        goal.setTitle(goalDTO.getTitle());
        goal.setDescricao(goalDTO.getDescription());
        goal.setCompleted(goalDTO.isCompleted());
        goal.setUser(userRepository.findById(goalDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User não encontrado")));
        goal = goalRepository.save(goal);
        return convertToDTO(goal);
    }


    public GoalDTO getGoalById(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada"));
        return convertToDTO(goal);
    }

    public List<GoalDTO> getAllGoals() {
        return goalRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<GoalDTO> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GoalDTO convertToDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescricao());
        dto.setCompleted(goal.isCompleted());
        dto.setUserId(goal.getUser().getId());
        return dto;
    }


}
