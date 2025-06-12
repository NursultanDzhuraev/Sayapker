package pro.sayapker.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sayapker.dto.authentication.*;
import pro.sayapker.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @Operation(summary = "Вход", description = "Позволяет пользователю войти в систему")
    @PostMapping("/singIn")
    public AuthResponse singIn(@RequestBody @Valid SignInRequest signInRequest) {
        return  authService.singIn(signInRequest);
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestBody @Valid EmailRequest request) {
      return   authService.sendOtpToEmail(request);
    }

    @PostMapping("/verifyAndSignUp")
    public AuthResponse verifyOtpAndSignUp(@RequestBody @Valid VerifyRequest request) {
        return authService.verifyOtpAndSignUp(request);
    }

    @Operation(summary = "Регистрация клиента", description = "Регистрация нового клиента")
    @PostMapping("/singUpForClient")
    public ResponseEntity<AuthResponse> singUpForClient(@RequestBody @Valid UserSingRequest userSignUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUpForClient(userSignUpRequest));
    }
}
