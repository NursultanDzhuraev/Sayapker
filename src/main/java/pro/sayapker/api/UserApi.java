package pro.sayapker.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.user.UserRequest;
import pro.sayapker.dto.user.UserResponse;
import pro.sayapker.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @Secured("ADMIN")
    @Operation(summary = "Получить users ", description = "Только админ может получить")
    @GetMapping("/findAllUsers")
    public PaginationResponse<UserResponse> findAllUsers(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize) {
        return userService.findAllUsers(pageNumber, pageSize);
    }
    @Secured("ADMIN")
    @Operation(summary = "Удалить user", description = "Только администратор может удалить user")
    @DeleteMapping("/deleteUserById/{userId}")
    public SimpleResponse deleteUserById(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }
    @Secured("USER")
    @Operation(summary = "Обновить личный профиль пользователя")
    @PutMapping("/updatedUser")
    public ResponseEntity<?> updatedUser(@RequestBody UserRequest userRequest){
        return userService.updatedUser(userRequest);
    }
    @Operation(summary = "Получить user")
    @PostMapping("/findUserByHorseId/{horseId}")
    public UserResponse findUserByHorseId(@PathVariable Long horseId) {
        return userService.findUserByHorseId(horseId);
    }
}
