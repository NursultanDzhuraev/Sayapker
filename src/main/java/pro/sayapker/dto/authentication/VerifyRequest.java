package pro.sayapker.dto.authentication;

import jakarta.validation.constraints.NotBlank;


public record VerifyRequest(
        @NotBlank
        String otp
) {
}
