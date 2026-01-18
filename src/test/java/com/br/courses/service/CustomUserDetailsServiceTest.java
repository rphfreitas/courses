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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword123");
        testUser.setEnabled(true);
        testUser.setRole("ROLE_USER");
    }

    @Test
    @DisplayName("Deve carregar detalhes do usuário com sucesso")
    void shouldLoadUserByUsernameSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("encodedPassword123");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Deve carregar autoridades corretas do usuário")
    void shouldLoadUserWithCorrectAuthorities() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertThat(result.getAuthorities()).isNotEmpty();
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Deve carregar usuário com role de administrador")
    void shouldLoadUserWithAdminRole() {
        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("adminPassword");
        adminUser.setEnabled(true);
        adminUser.setRole("ROLE_ADMIN");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado: nonexistent");

        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Deve carregar usuário desabilitado corretamente")
    void shouldLoadDisabledUserCorrectly() {
        User disabledUser = new User();
        disabledUser.setId(3L);
        disabledUser.setUsername("disableduser");
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setPassword("password");
        disabledUser.setEnabled(false);
        disabledUser.setRole("ROLE_USER");

        when(userRepository.findByUsername("disableduser")).thenReturn(Optional.of(disabledUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("disableduser");

        assertThat(result.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Deve chamar repositório uma única vez ao carregar usuário")
    void shouldCallRepositoryOncePerLoadUserRequest() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        customUserDetailsService.loadUserByUsername("testuser");

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Deve ser sensível a maiúsculas e minúsculas no username")
    void shouldBeCaseSensitiveForUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.empty());

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("TestUser"))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByUsername("TestUser");
    }

    @Test
    @DisplayName("Deve manter dados originais do usuário ao carregar detalhes")
    void shouldMaintainOriginalUserDataWhenLoadingDetails() {
        User userWithDetails = new User();
        userWithDetails.setId(100L);
        userWithDetails.setUsername("user100");
        userWithDetails.setEmail("user100@example.com");
        userWithDetails.setPassword("securePassword");
        userWithDetails.setEnabled(true);
        userWithDetails.setRole("ROLE_MANAGER");

        when(userRepository.findByUsername("user100")).thenReturn(Optional.of(userWithDetails));

        UserDetails result = customUserDetailsService.loadUserByUsername("user100");

        assertThat(result.getUsername()).isEqualTo("user100");
        assertThat(result.getPassword()).isEqualTo("securePassword");
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_MANAGER");
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException com mensagem apropriada")
    void shouldThrowUsernameNotFoundExceptionWithAppropriateMessage() {
        String username = "unknownuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado: " + username);
    }

    @Test
    @DisplayName("Deve carregar usuário com múltiplas chamadas ao repositório")
    void shouldLoadMultipleUsersCorrectly() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setEnabled(true);
        user2.setRole("ROLE_USER");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));

        UserDetails result1 = customUserDetailsService.loadUserByUsername("testuser");
        UserDetails result2 = customUserDetailsService.loadUserByUsername("user2");

        assertThat(result1.getUsername()).isEqualTo("testuser");
        assertThat(result2.getUsername()).isEqualTo("user2");

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByUsername("user2");
    }

    @Test
    @DisplayName("Deve manter flags de segurança como true quando usuário habilitado")
    void shouldMaintainSecurityFlagsTrueForEnabledUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
        assertThat(result.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Deve carregar usuário com caracteres especiais no username")
    void shouldLoadUserWithSpecialCharactersInUsername() {
        User userWithSpecialChars = new User();
        userWithSpecialChars.setId(4L);
        userWithSpecialChars.setUsername("user_@123");
        userWithSpecialChars.setEmail("special@example.com");
        userWithSpecialChars.setPassword("password");
        userWithSpecialChars.setEnabled(true);
        userWithSpecialChars.setRole("ROLE_USER");

        when(userRepository.findByUsername("user_@123")).thenReturn(Optional.of(userWithSpecialChars));

        UserDetails result = customUserDetailsService.loadUserByUsername("user_@123");

        assertThat(result.getUsername()).isEqualTo("user_@123");
    }
}

