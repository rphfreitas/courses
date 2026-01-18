package com.br.courses.service;

import com.br.courses.model.User;
import com.br.courses.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setEnabled(true);
        testUser.setRole("ROLE_USER");
    }

    // ================== REGISTER USER TESTS ==================

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void testRegisterUserSuccess() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.registerUser(newUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("newuser");
        assertThat(result.getEmail()).isEqualTo("newuser@example.com");
        verify(userRepository, times(1)).findByUsername("newuser");
        verify(userRepository, times(1)).findByEmail("newuser@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando username já existe")
    void testRegisterUserWithExistingUsername() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("existinguser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password123");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username já existe");

        verify(userRepository, times(1)).findByUsername("existinguser");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void testRegisterUserWithExistingEmail() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("existing@example.com");
        newUser.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já existe");

        verify(userRepository, times(1)).findByUsername("newuser");
        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve definir role padrão como ROLE_USER quando não informado")
    void testRegisterUserWithDefaultRole() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");
        newUser.setRole(null);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Act
        User result = userService.registerUser(newUser);

        // Assert
        assertThat(result.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Deve criptografar a senha antes de salvar")
    void testRegisterUserPasswordEncryption() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setEmail("test@example.com");
        newUser.setPassword("plainPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        userService.registerUser(newUser);

        // Assert
        verify(passwordEncoder, times(1)).encode("plainPassword");
    }

    // ================== FIND BY USERNAME TESTS ==================

    @Test
    @DisplayName("Deve encontrar usuário pelo username")
    void testFindByUsernameSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByUsername("testuser");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando username não existe")
    void testFindByUsernameNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByUsername("nonexistent");

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    // ================== FIND BY EMAIL TESTS ==================

    @Test
    @DisplayName("Deve encontrar usuário pelo email")
    void testFindByEmailSuccess() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByEmail("test@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando email não existe")
    void testFindByEmailNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    // ================== GET USER BY ID TESTS ==================

    @Test
    @DisplayName("Deve obter usuário pelo ID")
    void testGetUserByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando usuário não existe")
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(999L);
    }

    // ================== GET ALL USERS TESTS ==================

    @Test
    @DisplayName("Deve listar todos os usuários")
    void testGetAllUsersSuccess() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password456");
        user2.setEnabled(true);
        user2.setRole("ROLE_ADMIN");

        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testUser, user2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void testGetAllUsersEmpty() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    // ================== UPDATE USER TESTS ==================

    @Test
    @DisplayName("Deve atualizar email do usuário")
    void testUpdateUserEmailSuccess() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail("newemail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, userDetails);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar role do usuário")
    void testUpdateUserRoleSuccess() {
        // Arrange
        User userDetails = new User();
        userDetails.setRole("ROLE_ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, userDetails);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar enabled do usuário")
    void testUpdateUserEnabledSuccess() {
        // Arrange
        User userDetails = new User();
        userDetails.setEnabled(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, userDetails);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar múltiplos campos do usuário")
    void testUpdateUserMultipleFieldsSuccess() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail("newemail@example.com");
        userDetails.setRole("ROLE_ADMIN");
        userDetails.setEnabled(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, userDetails);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado na atualização")
    void testUpdateUserNotFound() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail("newemail@example.com");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(999L, userDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve não atualizar campos nulos")
    void testUpdateUserWithNullFields() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail(null);
        userDetails.setRole(null);
        userDetails.setEnabled(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, userDetails);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ================== DELETE USER TESTS ==================

    @Test
    @DisplayName("Deve deletar usuário por ID")
    void testDeleteUserSuccess() {
        // Arrange
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve tentar deletar usuário que não existe")
    void testDeleteUserNotFound() {
        // Arrange
        doNothing().when(userRepository).deleteById(999L);

        // Act
        userService.deleteUser(999L);

        // Assert
        verify(userRepository, times(1)).deleteById(999L);
    }
}

