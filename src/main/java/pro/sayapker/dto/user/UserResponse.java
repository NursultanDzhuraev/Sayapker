package pro.sayapker.dto.user;

import lombok.Builder;
import pro.sayapker.entity.User;

import java.util.List;

@Builder
public record UserResponse(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
    public static UserResponse dtoToEntity(User user) {
        return   UserResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }
    public static List<UserResponse> entityToDtoList(List<User> users) {
        return   users.stream().map(UserResponse::dtoToEntity).toList();
    }
}
