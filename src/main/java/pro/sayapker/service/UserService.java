package pro.sayapker.service;

import org.springframework.http.ResponseEntity;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.user.UserRequest;
import pro.sayapker.dto.user.UserResponse;

import java.util.List;

public interface UserService {


    List<UserResponse> findAllUsers(int pageNumber, int pageSize);

    SimpleResponse deleteUserById(Long userId);

    ResponseEntity<?> updatedUser(UserRequest userRequest);

    UserResponse findUserByHorseId(Long horseId);
}


