package org.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.bankcards.dto.request.AuthRequest;
import org.example.bankcards.dto.request.RegisterRequest;
import org.example.bankcards.dto.response.AuthResponse;
import org.example.bankcards.pojo.Role;
import org.example.bankcards.service.AuthService.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Неверные данные")
    })
    @PostMapping("/register/user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.USER));
    }


    @Operation(summary = "Регистрация администратора")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Администратор успешно зарегистрирован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (недостаточно прав)")
    })
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.ADMIN));
    }


    @Operation(summary = "Вход в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
