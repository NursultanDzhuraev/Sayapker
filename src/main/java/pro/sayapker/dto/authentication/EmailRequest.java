package pro.sayapker.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pro.sayapker.validation.email.ValidEmail;
import pro.sayapker.validation.password.ValidPassword;

@Getter
@Setter
public class EmailRequest {
    @ValidEmail
    @NotBlank
    private String email;
    @ValidPassword
    private String password;
}
