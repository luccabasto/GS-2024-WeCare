package com.wecare.wecare_backend.service;

import com.wecare.wecare_backend.dto.UserDTO;
import com.wecare.wecare_backend.model.User;
import com.wecare.wecare_backend.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    public class CPFUtils {
        // Método para formatar o CPF no padrão XXX.XXX.XXX-XX
        public static String formatCPF(String cpf) {
            if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid CPF. Must be 11 digits.");
            }
            return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
    }
// Procedrues estão quebrando o código mesmo fazendo a requisisçã
//    public void insertUser(String nome, int idade, String motivacao, String email, String cpf, String endereco) {
//
//        String formattedCPF = CPFUtils.formatCPF(cpf);
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_user");
//        query.registerStoredProcedureParameter("p_nome", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("p_idade", Integer.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("p_motivacao", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("p_cpf", String.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter("p_endereco", String.class, ParameterMode.IN);
//
//        query.setParameter("p_nome", nome);
//        query.setParameter("p_idade", idade);
//        query.setParameter("p_motivacao", motivacao);
//        query.setParameter("p_email", email);
//        query.setParameter("p_cpf",formattedCPF);
//        query.setParameter("p_endereco", endereco);
//
//        query.execute();
//    }

    public User createUser(String nome, int idade, String motivacao, String email, String cpf, String endereco) {
        User user = new User();
        user.setNome(nome);
        user.setIdade(idade);
        user.setMotivacao(motivacao);
        user.setEmail(email);
        user.setCpf(cpf);
        user.setEndereco(endereco);

        return userRepository.save(user);
    }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String exportUserGoals(Long userId) {
        return userRepository.exportUserGoals(userId);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        return convertToDTO(user);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setNome(dto.getNome());
        user.setIdade(dto.getIdade());
        user.setMotivacao(dto.getMotivacao());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setEndereco(dto.getEndereco());
        return user;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getNome(),
                user.getIdade(),
                user.getMotivacao(),
                user.getEmail(),
                user.getCpf(),
                user.getEndereco()
        );
    }

}