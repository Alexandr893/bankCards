package org.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.bankcards.pojo.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role cannot be null")
    private Role role;

    public UserDto(Long id, String username) {
        this.id = id;
        this.email = username;
    }
}
