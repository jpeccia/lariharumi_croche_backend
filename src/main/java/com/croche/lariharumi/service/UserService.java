package com.croche.lariharumi.service;

import com.croche.lariharumi.dto.UserDTO;
import com.croche.lariharumi.dto.UserUpdateDTO;
import com.croche.lariharumi.exceptions.UserNotFoundException;
import com.croche.lariharumi.models.User.User;
import com.croche.lariharumi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Obter todos os usuários
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    // Obter usuário por ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    // Atualizar usuário
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));

        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());

        User updatedUser = userRepository.save(user);

        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
    }

    // Deletar usuário
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado."));
        userRepository.delete(user);
    }
}
