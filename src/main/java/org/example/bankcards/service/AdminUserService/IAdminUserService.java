package org.example.bankcards.service.AdminUserService;

import org.example.bankcards.dto.UserDto;

import java.util.List;

public interface IAdminUserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);

}
