package pro.sayapker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.sayapker.config.JwtService;
import pro.sayapker.dto.authentication.*;
import pro.sayapker.entity.User;
import pro.sayapker.enums.Role;
import pro.sayapker.exception.AlreadyExistsException;
import pro.sayapker.exception.BadRequestException;
import pro.sayapker.repository.UserRepo;
import pro.sayapker.service.AuthService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private static final int EXPIRE_MINUTES = 5;
    private final Map<String,EmailRequest> emailStore = new ConcurrentHashMap<>();

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
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse signUpForClient(UserSingRequest request) {
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
        return new AuthResponse(user.getId(), user.getEmail(), token, user.getRole());
    }

    @Override
    public AuthResponse verifyOtpAndSignUp(VerifyRequest request) {
        String otp = request.otp();
        String matchedEmail = otpStore.entrySet().stream()
                .filter(entry -> entry.getValue().getOtp().equals(otp))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (matchedEmail == null || !validateOtp(matchedEmail, otp)) {
            throw new IllegalArgumentException("Код туура эмес же мөөнөтү өтүп кеткен");
        }
        EmailRequest emailRequest = emailStore.remove(matchedEmail);
        if (emailRequest == null) {
            throw new IllegalStateException("Каттоо маалыматы табылган жок.");
        }
        User user = new User();
        user.setEmail(emailRequest.getEmail());
        user.setPassword(passwordEncoder.encode(emailRequest.getPassword()));
        user.setRole(Role.USER);

                userRepo.save(user);

                String token = jwtService.generateToken(user);

        return new AuthResponse(user.getId(), user.getEmail(), token, user.getRole());
    }

    @Override
    public ResponseEntity<String> sendOtpToEmail(EmailRequest request) {
        String otp = generateSecureOtp(6);
        String email = request.getEmail();
        User user = userRepo.findByEmail(email).orElse(null);
        if(user !=null){
            throw new AlreadyExistsException(email+"мурунтан эле катталган");
        }

        saveOtp(email, otp);
        emailStore.put(email, request);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Сиздин OTP код");
        message.setText("Сиздин бир жолку сырсөз (OTP): " + otp +
                "\nАл 5 мүнөт ичинде колдонулушу керек.");

        mailSender.send(message);
        return ResponseEntity.status(HttpStatus.OK).body("OTP жөнөтүлдү: " + email);
    }

   private boolean validateOtp(String email, String inputOtp) {
        if (!otpStore.containsKey(email)) return false;
        OtpData data = otpStore.get(email);
        if (LocalDateTime.now().isAfter(data.getExpiryTime())) {
            otpStore.remove(email);
            return false;
        }

        boolean isValid = data.getOtp().equals(inputOtp);
        if (isValid) otpStore.remove(email);
        return isValid;
    }
    public void saveOtp(String email, String otp) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(EXPIRE_MINUTES);
        otpStore.put(email, new OtpData(otp, expiryTime));
    }
    @Override
    public String generateSecureOtp(int length) {
        SecureRandom secureRandom = new SecureRandom();
        String digits = "0123456789";
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(digits.length());
            otp.append(digits.charAt(index));
        }

        return otp.toString();
    }

    @Scheduled(fixedRate = 600_000)
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();

        otpStore.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getExpiryTime()));
        emailStore.keySet().removeIf(email -> !otpStore.containsKey(email));
    }
    static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
