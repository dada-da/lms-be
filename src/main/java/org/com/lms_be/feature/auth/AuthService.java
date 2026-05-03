package org.com.lms_be.feature.auth;

import lombok.RequiredArgsConstructor;
import org.com.lms_be.config.JwtUtil;
import org.com.lms_be.exception.InvalidCredentialsException;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new LoginResponseDTO(user.getId(), user.getEmail(), user.getRole(), token);
    }
}
