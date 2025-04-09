package org.example.bankcards.mapper;

import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        return user;
    }
}
