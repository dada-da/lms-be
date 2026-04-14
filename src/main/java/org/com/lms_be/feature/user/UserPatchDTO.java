package org.com.lms_be.feature.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.UserRole;

@Getter
@Setter
public class UserPatchDTO {

    @Email
    private String email;    // null means "don't change"

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;    // null means "don't change"

    private UserRole role;      // null means "don't change"
}