package com.wecare.wecare_backend.controller;

import com.wecare.wecare_backend.dto.UserDTO;
import com.wecare.wecare_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.saveUser(userDTO);
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null); // Erro genérico
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id); // Usa o método direto do serviço
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
