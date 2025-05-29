package pro.sayapker.service;

import jakarta.validation.Valid;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.authentication.SignInRequest;
import pro.sayapker.dto.authentication.UserSignUpRequest;

public interface AuthService {
    AuthResponse singIn(@Valid SignInRequest signInRequest);

    AuthResponse signUpForClient(@Valid UserSignUpRequest userSignUpRequest);
}
