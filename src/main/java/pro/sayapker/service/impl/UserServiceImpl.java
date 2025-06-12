package pro.sayapker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.sayapker.config.JwtService;
import pro.sayapker.dto.PaginationResponse;
import pro.sayapker.dto.SimpleResponse;
import pro.sayapker.dto.authentication.AuthResponse;
import pro.sayapker.dto.authentication.VerifyRequest;
import pro.sayapker.dto.user.UserRequest;
import pro.sayapker.dto.user.UserResponse;
import pro.sayapker.entity.User;
import pro.sayapker.exception.BadRequestException;
import pro.sayapker.exception.NotFoundException;
import pro.sayapker.repository.UserRepo;
import pro.sayapker.repository.jdbcClient.UserJDBC;
import pro.sayapker.service.AuthService;
import pro.sayapker.service.UserService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserJDBC userJDBC;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtService jwtService;
    private final Map<String, AuthServiceImpl.OtpData> otpsStore = new ConcurrentHashMap<>();
    private static final int EXPIRE_MINUTES = 5;
    private final Map<String, UserRequest> emailsStore = new ConcurrentHashMap<>();
    @Override
    public PaginationResponse<UserResponse> findAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<User> users = userRepo.findAllUsers(pageable);
        var response = new PaginationResponse<UserResponse>();
        response.setPageNumber(pageNumber+1);
        response.setPageSize(pageSize);
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
        response.setContent(UserResponse.entityToDtoList(users.getContent()));
        return response;
    }
    @Override
    public SimpleResponse deleteUserById(Long userId) {
        User user = userRepo.findByIdlOrElseThrow(userId);
       return userJDBC.deleteUser(user.getId());
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
                user.setImageUrl(userRequest.imageUrl());
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

            String otp = authService.generateSecureOtp(6);
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(EXPIRE_MINUTES);
            otpsStore.put(user.getEmail(), new AuthServiceImpl.OtpData(otp, expiryTime));
            emailsStore.put(user.getEmail(), userRequest);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userRequest.email());
            message.setSubject("Email өзгөртүү үчүн OTP");
            message.setText("Сиздин бир жолку OTP: " + otp +
                    "\nБул код 5 мүнөт ичинде колдонулушу керек.");
            mailSender.send(message);

            return ResponseEntity.ok("Email өзгөртүү үчүн OTP жөнөтүлдү: " + userRequest.email());
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

    @Override
    public UserResponse findByIdUser(Long userId) {
        User user = userRepo.findByIdlOrElseThrow(userId);
        return UserResponse.dtoToEntity(user);
    }

    @Override
    public ResponseEntity<?> verifyOtpForEmailChange(VerifyRequest request) {
        String otp = request.otp();
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmailOrElseThrow(currentEmail);
        UserRequest request2 = emailsStore.get(currentEmail);
        if (request2 == null || !validateOtp(currentEmail, otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP туура эмес же мөөнөтү өтүп кеткен");
        }
        user.setFirstName(request2.firstName());
        user.setLastName(request2.lastName());
        user.setPassword(passwordEncoder.encode(request2.password()));
        user.setPhoneNumber(request2.phoneNumber());
        user.setImageUrl(request2.imageUrl());
        user.setEmail(request2.email());

        User save = userRepo.save(user);
        String token = jwtService.generateToken(save);

        return ResponseEntity.ok(AuthResponse.builder()
                .id(save.getId())
                .token(token)
                .email(save.getEmail())
                .role(save.getRole())
                .build());
    }
    private boolean validateOtp(String email, String inputOtp) {
        if (!otpsStore.containsKey(email)) return false;
        AuthServiceImpl.OtpData data = otpsStore.get(email);
        if (LocalDateTime.now().isAfter(data.getExpiryTime())) {
            otpsStore.remove(email);
            return false;
        }
        boolean isValid = data.getOtp().equals(inputOtp);
        if (isValid) otpsStore.remove(email);
        return isValid;
    }
    @Scheduled(fixedRate = 600_000)
    public void cleanupExpiredOtp() {
        LocalDateTime now = LocalDateTime.now();

        otpsStore.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getExpiryTime()));
        emailsStore.keySet().removeIf(email -> !otpsStore.containsKey(email));
    }
}
