package pro.sayapker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.sayapker.config.JwtService;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.user.UserRequest;
import pro.sayapker.dto.user.UserResponse;
import pro.sayapker.entity.User;
import pro.sayapker.exception.BadRequestException;
import pro.sayapker.exception.NotFoundException;
import pro.sayapker.repository.UserRepo;
import pro.sayapker.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public List<UserResponse> findAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepo.findAllUsers(pageable);
        return users.getContent().stream().map(user -> new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        )).collect(Collectors.toList());
    }
    @Override
    public SimpleResponse deleteUserById(Long userId) {
        User user = userRepo.findByIdlOrElseThrow(userId);
        userRepo.delete(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешна уделена")
                .build();
    }
    @Override
    public ResponseEntity<?> updatedUser(UserRequest userRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmailOrElseThrow(email);
        if (email.equals(user.getEmail())) {
            if (user.getEmail().matches(userRequest.email())) {
                user.setFirstName(userRequest.firstName());
                user.setLastName(userRequest.lastName());
                user.setPassword(passwordEncoder.encode(userRequest.password()));
                user.setPhoneNumber(userRequest.phoneNumber());
                userRepo.save(user);
                return ResponseEntity.ok(SimpleResponse
                        .builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Успешно обновлено")
                        .build());
            }
            if (userRepo.findByEmail(userRequest.email()).isPresent()) {
                throw new BadRequestException("Пользователь с таким email уже существует");
            }
            user.setFirstName(userRequest.firstName());
            user.setLastName(userRequest.lastName());
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            user.setPhoneNumber(userRequest.phoneNumber());
            user.setEmail(userRequest.email());
            userRepo.save(user);
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthResponse.builder()
                    .id(user.getId())
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .firstName(user.getFirstName())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка в обновления");
    }
    @Override
    public UserResponse findUserByHorseId(Long horseId) {
       User user = userRepo.findUserByHorseId(horseId);
       if(user == null) {throw new NotFoundException("Пользователь не найден");}
       return UserResponse.builder()
               .userId(user.getId())
               .firstName(user.getFirstName())
               .lastName(user.getLastName())
               .email(user.getEmail())
               .phoneNumber(user.getPhoneNumber())
               .build();
    }
}
