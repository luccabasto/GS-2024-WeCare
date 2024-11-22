package com.wecare.wecare_backend.repository;

import com.wecare.wecare_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
