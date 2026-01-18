package com.br.courses.mapper;

import com.br.courses.dto.UserResponse;
import com.br.courses.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para converter entidades User em DTOs UserResponse
 */
@Component
public class UserMapper {

    /**
     * Converte uma entidade User para UserResponse
     *
     * @param user entidade User a ser convertida
     * @return UserResponse ou null se user for null
     */
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getEnabled(),
            user.getRole()
        );
    }

    /**
     * Converte uma lista de User para lista de UserResponse
     *
     * @param users lista de User a ser convertida
     * @return lista de UserResponse
     */
    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream()
            .map(this::toResponse)
            .toList();
    }
}

