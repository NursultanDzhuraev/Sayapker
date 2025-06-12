package pro.sayapker.service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.authentication.VerifyRequest;
import pro.sayapker.dto.user.UserRequest;
import pro.sayapker.dto.user.UserResponse;


public interface UserService {

    PaginationResponse<UserResponse> findAllUsers(int pageNumber, int pageSize);

    SimpleResponse deleteUserById(Long userId);

    ResponseEntity<?> updatedUser(UserRequest userRequest);

    UserResponse findUserByHorseId(Long horseId);

    UserResponse findByIdUser(Long userId);

    ResponseEntity<?> verifyOtpForEmailChange(@Valid VerifyRequest request);
}


