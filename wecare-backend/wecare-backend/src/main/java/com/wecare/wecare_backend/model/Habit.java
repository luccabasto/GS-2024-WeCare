package com.wecare.wecare_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;


@Entity
@Table(name = "habits")
public class Habit extends RepresentationModel<Habit> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do hábito é obrigatório")
    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime criado_em = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCriado_em() {
        return criado_em;
    }

    public void setCriado_em(LocalDateTime criado_em) {
        this.criado_em = criado_em;
    }
}