package org.com.lms_be.feature.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.com.lms_be.util.UserRole;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String email;
    private UserRole role;
    private String token;
}
