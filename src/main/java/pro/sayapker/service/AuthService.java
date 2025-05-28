package pro.sayapker.service;

import jakarta.validation.Valid;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.authentication.SignInRequest;

public interface AuthService {
    AuthResponse singIn(@Valid SignInRequest signInRequest);
}
