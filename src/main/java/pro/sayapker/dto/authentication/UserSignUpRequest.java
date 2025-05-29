package pro.sayapker.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import pro.sayapker.validation.email.ValidEmail;
import pro.sayapker.validation.password.ValidPassword;

public record UserSignUpRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        String firstName,
        @NotBlank(message = "Email обязателен")
        @ValidEmail
        String email,
        @NotBlank(message = "Пароль обязателен")
        @ValidPassword
        String password,
        String lastName,
        String phoneNumber,
        String imageUrl
) {
}
