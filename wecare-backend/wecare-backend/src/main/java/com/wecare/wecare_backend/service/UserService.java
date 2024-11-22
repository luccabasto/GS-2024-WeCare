package com.wecare.wecare_backend.service;

import com.wecare.wecare_backend.dto.UserDTO;
import com.wecare.wecare_backend.model.User;
import com.wecare.wecare_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
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
