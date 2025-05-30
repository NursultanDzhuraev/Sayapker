package pro.sayapker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.sayapker.config.JwtService;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.authentication.SignInRequest;
import pro.sayapker.dto.authentication.UserSignUpRequest;
import pro.sayapker.entity.User;
import pro.sayapker.enums.Role;
import pro.sayapker.exception.BadRequestException;
import pro.sayapker.exception.IllegalArgumentException;
import pro.sayapker.repository.UserRepo;
import pro.sayapker.service.AuthService;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public AuthResponse singIn(SignInRequest signInRequest) {
        User user = userRepo.findByEmailOrElseThrow(signInRequest.email());
        if(!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        String token = jwtService.generateToken(user);
      return   AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse signUpForClient(UserSignUpRequest request) {
        if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setPhoneNumber(request.phoneNumber());
        user.setImageUrl(request.imageUrl());
        userRepo.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(user.getId(), user.getFirstName(), user.getEmail(), token, user.getRole());
    }
}
