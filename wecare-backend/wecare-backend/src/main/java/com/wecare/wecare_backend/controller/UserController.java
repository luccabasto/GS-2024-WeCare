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

    @GetMapping("/{userId}/goals")
    public ResponseEntity<String> exportUserGoals(@PathVariable Long userId) {
        String result = userService.exportUserGoals(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertUser(
            @RequestParam String nome,
            @RequestParam int idade,
            @RequestParam String motivacao,
            @RequestParam String email,
            @RequestParam String cpf,
            @RequestParam String endereco) {
        userService.insertUser(nome, idade, motivacao, email, cpf, endereco);
        return ResponseEntity.ok("Usuário inserido com sucesso!");
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.insertUser(
                userDTO.getNome(), userDTO.getIdade(), userDTO.getMotivacao(), userDTO.getEmail(), userDTO.getCpf(), userDTO.getEndereco());
        return ResponseEntity.ok("User criado com sucesso");
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
