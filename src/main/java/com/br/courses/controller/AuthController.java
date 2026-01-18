package com.br.courses.controller;

import com.br.courses.dto.LoginRequest;
import com.br.courses.dto.LoginResponse;
import com.br.courses.dto.RefreshTokenRequest;
import com.br.courses.dto.UserResponse;
import com.br.courses.mapper.UserMapper;
import com.br.courses.model.User;
import com.br.courses.security.JwtTokenProvider;
import com.br.courses.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação com JWT e gerenciamento de usuários")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica um usuário e retorna tokens JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(loginRequest.username());

            return ResponseEntity.ok(LoginResponse.of(
                    accessToken,
                    refreshToken,
                    3600L, // 1 hora em segundos
                    loginRequest.username()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Credenciais inválidas\"}");
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Renovar token de acesso", description = "Gera um novo access token usando o refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.refreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Refresh token inválido ou expirado\"}");
        }

        String username = jwtTokenProvider.getUsernameFromToken(request.refreshToken());
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(username, null)
        );

        return ResponseEntity.ok(LoginResponse.of(
                newAccessToken,
                request.refreshToken(),
                3600L,
                username
        ));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou username/email já existe",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserResponse> register(@RequestBody @Valid User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(registeredUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários (requer autenticação)")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.toResponseList(users));
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obter usuário por ID", description = "Retorna os detalhes de um usuário específico (requer autenticação)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(userMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente (requer autenticação)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(userMapper.toResponse(updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema (requer autenticação)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

