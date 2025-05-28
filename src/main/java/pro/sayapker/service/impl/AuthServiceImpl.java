package pro.sayapker.service.impl;

import org.springframework.stereotype.Service;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.authentication.SignInRequest;
import pro.sayapker.service.AuthService;
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse singIn(SignInRequest signInRequest) {

        return null;
    }
}
