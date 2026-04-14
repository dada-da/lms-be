package org.com.lms_be.feature.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.UserRole;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private UserRole role;
    private Instant createdDate;
}