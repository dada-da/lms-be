package org.com.lms_be.feature.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.UserRole;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Enum<UserRole> role;
    private Instant createdDate;
}
