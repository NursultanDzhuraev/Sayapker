package pro.sayapker.service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import pro.sayapker.dto.authentication.*;

public interface AuthService {
    AuthResponse singIn(@Valid SignInRequest signInRequest);

    AuthResponse signUpForClient(@Valid UserSingRequest userSignUpRequest);

    AuthResponse verifyOtpAndSignUp(@Valid VerifyRequest request);

    ResponseEntity<String> sendOtpToEmail(@Valid EmailRequest request);

    String generateSecureOtp(int length);
}
