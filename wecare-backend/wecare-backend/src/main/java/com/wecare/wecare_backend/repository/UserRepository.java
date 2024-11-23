package com.wecare.wecare_backend.repository;

import com.wecare.wecare_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query(value = "CALL insert_user(:nome, :idade, :motivacao, :email, :cpf, :endereco)", nativeQuery = true)
    void insertUser(
            @Param("nome") String nome,
            @Param("idade") Integer idade,
            @Param("motivacao") String motivacao,
            @Param("email") String email,
            @Param("cpf") String cpf,
            @Param("endereco") String endereco
    );

    @Query(value = "CALL export_user_goals(:userId)", nativeQuery = true)
    String exportUserGoals(@Param("userId") Long userId);


}
