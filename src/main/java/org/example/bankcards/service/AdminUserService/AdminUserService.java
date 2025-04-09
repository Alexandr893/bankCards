package org.example.bankcards.service.AdminUserService;


import lombok.AllArgsConstructor;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.UserRepository;
import org.example.bankcards.dto.UserDto;
import org.example.bankcards.mapper.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminUserService implements IAdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }



}
