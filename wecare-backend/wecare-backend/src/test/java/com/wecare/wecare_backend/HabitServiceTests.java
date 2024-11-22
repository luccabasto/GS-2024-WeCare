package com.wecare.wecare_backend;

import com.wecare.wecare_backend.model.Habit;
import com.wecare.wecare_backend.repository.HabitRepository;
import com.wecare.wecare_backend.service.HabitService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HabitServiceTests {

    private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
    private final HabitService habitService = new HabitService(habitRepository);

    @Test
    public void testGetUserHabits() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setNome("Morning Run");

        when(habitRepository.findByUserId(1L)).thenReturn(Collections.singletonList(habit));

        List<Habit> habits = habitService.getUserHabits(1L);

        assertEquals(1, habits.size());
        assertEquals("Morning Run", habits.get(0).getNome());
        verify(habitRepository, times(1)).findByUserId(1L);
    }
}
