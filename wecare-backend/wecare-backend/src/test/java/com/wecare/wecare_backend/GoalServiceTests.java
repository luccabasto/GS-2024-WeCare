package com.wecare.wecare_backend;

import com.wecare.wecare_backend.dto.GoalDTO;
import com.wecare.wecare_backend.model.Goal;
import com.wecare.wecare_backend.repository.GoalRepository;
import com.wecare.wecare_backend.service.GoalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GoalServiceTests {

    private final GoalRepository goalRepository = Mockito.mock(GoalRepository.class);
    private final GoalService goalService = new GoalService(goalRepository);

    @Test
    public void testGetUserGoals() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Lose Weight");

        when(goalRepository.findByUserId(1L)).thenReturn(Collections.singletonList(goal));

        List<GoalDTO> goals = goalService.getGoalsByUserId(1L);

        assertEquals(1, goals.size());
        assertEquals("Lose Weight", goals.get(0).getTitle());
        verify(goalRepository, times(1)).findByUserId(1L);
    }
}
