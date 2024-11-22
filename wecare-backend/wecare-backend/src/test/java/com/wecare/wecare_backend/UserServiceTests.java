package com.wecare.wecare_backend;

import com.wecare.wecare_backend.dto.UserDTO;
import com.wecare.wecare_backend.model.User;
import com.wecare.wecare_backend.repository.UserRepository;
import com.wecare.wecare_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    public void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setNome("John Doe");
        user.setEmail("johndoe@example.com");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getNome());
        verify(userRepository, times(1)).findAll();
    }
}
