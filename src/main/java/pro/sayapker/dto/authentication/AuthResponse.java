package pro.sayapker.dto.authentication;

import pro.sayapker.enums.Role;

public record AuthResponse(
        Long id,
        String firstName,
        String email,
        String token,
        Role role
) {
}
