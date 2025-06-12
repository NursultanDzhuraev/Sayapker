package pro.sayapker.dto.authentication;

import lombok.Builder;
import pro.sayapker.enums.Role;
@Builder
public record AuthResponse(
        Long id,
        String email,
        String token,
        Role role
) {
}
