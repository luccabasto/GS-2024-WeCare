package com.wecare.wecare_backend.config;

import com.wecare.wecare_backend.service.GoalService;
import com.wecare.wecare_backend.service.HabitService;
import com.wecare.wecare_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Value("${app.seed-database:false}")
    private boolean seedDatabase;

    @Override
    public void run(String... args) throws Exception {

        if (!seedDatabase) {
            return; // Não executa se a flag estiver desativada
        }

        // Inserir usuários
        userService.createUser("Lucca Basto", 30, "Health", "mudando.doe@example.com", "12345658920", "Street 1");
        userService.createUser("Sa Zoe", 25, "Saúde Mental", "Zoe.tg@example.com", "04599823925", "Street 5");



        // Inserir metas
        goalService.insertGoal("Lose Weight", "Exercise daily", 1);
        goalService.insertGoal("Save Money", "Reduce expenses", 2);
        goalService.insertGoal("Run a Marathon", "Complete a marathon by end of year", 2);
        goalService.insertGoal("Read 12 Books", "Read one book per month", 3);

        // Inserir hábitos
        habitService.insertHabit("Exercise", 1);
        habitService.insertHabit("Track Expenses", 2);
        habitService.insertHabit("Healthy Eating", 1);
        habitService.insertHabit("Weekly Long Runs", 2);
        habitService.insertHabit("Monthly Book Reviews", 3);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private HabitService habitService;


}
