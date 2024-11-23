package com.wecare.wecare_backend;

import com.wecare.wecare_backend.dto.HabitDTO;
import com.wecare.wecare_backend.model.Habit;
import com.wecare.wecare_backend.repository.HabitRepository;
import com.wecare.wecare_backend.service.HabitService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class HabitServiceTests {

    private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
    private final HabitService habitService = new HabitService(habitRepository);

    @PersistenceContext
    private EntityManager entityManager;

    public void insertHabit(String name, int goalId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_habit");
        query.registerStoredProcedureParameter("p_nome", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_goal_id", Integer.class, ParameterMode.IN);

        query.setParameter("p_nome", name);
        query.setParameter("p_goal_id", goalId);

        query.execute();
    }

    @Test
    public void testGetUserHabits() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setNome("Morning Run");
        when(habitRepository.findByUserId(1L)).thenReturn(Collections.singletonList(habit));
        List<Habit> habits = habitService.getUserHabits(1L);
        when(habitRepository.findByGoalId(1L)).thenReturn(List.of(habit));
        List<HabitDTO> result = habitService.getHabitsByGoalId(1L);
        assertNotNull(result);
        assertEquals(1, habits.size());
        assertEquals("Morning Run", habits.get(0).getNome());
        verify(habitRepository, times(1)).findByUserId(1L);
    }
}
