package pro.sayapker.dto.user;

import lombok.Builder;

@Builder
public record UserResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
