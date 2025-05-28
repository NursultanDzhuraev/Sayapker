package pro.sayapker.dto.authentication;


import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "Email обязателен")
        String email,
        @NotBlank(message = "Пароль обязателен")
        String password
) {
}
