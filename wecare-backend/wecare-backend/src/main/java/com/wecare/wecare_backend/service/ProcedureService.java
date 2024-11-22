package com.wecare.wecare_backend.service;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Types;

@Service
public class ProcedureService {

    private final JdbcTemplate jdbcTemplate;

    public ProcedureService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Inserir Usuário
    public void insertUser(String nome, int idade, String motivacao, String email, String cpf, String endereco) {
        jdbcTemplate.update(con -> {
            CallableStatement stmt = con.prepareCall("{CALL insert_user(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, nome);
            stmt.setInt(2, idade);
            stmt.setString(3, motivacao);
            stmt.setString(4, email);
            stmt.setString(5, cpf);
            stmt.setString(6, endereco);
            return stmt;
        });
    }

    // Exportar Metas
    public String exportGoals(int userId) {
        return jdbcTemplate.execute((ConnectionCallback<String>) con -> {
            CallableStatement stmt = con.prepareCall("{CALL export_goals(?, ?)}");
            stmt.setInt(1, userId);
            stmt.registerOutParameter(2, Types.CLOB);
            stmt.execute();
            return stmt.getString(2); // Obtém o JSON como String
        });
    }
}
