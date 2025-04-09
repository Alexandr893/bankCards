package org.example.bankcards.service.AuthService;

import org.example.bankcards.dto.request.AuthRequest;
import org.example.bankcards.dto.request.RegisterRequest;
import org.example.bankcards.dto.response.AuthResponse;
import org.example.bankcards.pojo.Role;

public interface IAuthService {

    AuthResponse register(RegisterRequest request, Role role);
    AuthResponse login(AuthRequest request);

}
