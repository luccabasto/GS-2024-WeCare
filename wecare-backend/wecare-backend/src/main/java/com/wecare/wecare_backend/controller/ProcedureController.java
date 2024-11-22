package com.wecare.wecare_backend.controller;

import com.wecare.wecare_backend.service.ProcedureService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procedures")
public class ProcedureController {

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @PostMapping("/user")
    public String insertUser(@RequestParam String nome,
                             @RequestParam int idade,
                             @RequestParam String motivacao,
                             @RequestParam String email,
                             @RequestParam String cpf,
                             @RequestParam String endereco) {
        procedureService.insertUser(nome, idade, motivacao, email, cpf, endereco);
        return "Usuário inserido com sucesso!";
    }

    @GetMapping("/goals/{userId}")
    public String exportGoals(@PathVariable int userId) {
        return procedureService.exportGoals(userId);
    }
}
