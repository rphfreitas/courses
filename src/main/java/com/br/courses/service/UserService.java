package com.br.courses.service;

import com.br.courses.model.User;
import com.br.courses.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário no sistema
     * A senha é criptografada antes de ser armazenada
     */
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username já existe");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já existe");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        if (user.getRole() == null) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }

    /**
     * Encontra um usuário pelo username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Encontra um usuário pelo email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Obtém um usuário pelo ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Lista todos os usuários
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Atualiza um usuário
     */
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }
            if (userDetails.getEnabled() != null) {
                user.setEnabled(userDetails.getEnabled());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    /**
     * Deleta um usuário
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

