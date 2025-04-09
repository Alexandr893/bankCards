package org.example.bankcards.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.example.bankcards.dao.entity.User;
import org.example.bankcards.dao.repository.UserRepository;
import org.example.bankcards.dto.request.AuthRequest;
import org.example.bankcards.dto.request.RegisterRequest;
import org.example.bankcards.dto.response.AuthResponse;
import org.example.bankcards.exception.EmailAlreadyExistsException;
import org.example.bankcards.pojo.Role;
import org.example.bankcards.utill.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request, Role role) {
        if (role != Role.USER && role != Role.ADMIN) {
            throw new IllegalArgumentException("Недопустимая роль пользователя.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует.");
        }

        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        var jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверные учетные данные");
        }
        var jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
