package org.com.lms_be.feature.enrollment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.util.EnrollmentStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "enrollment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
public class EnrollmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16, columnDefinition = "VARCHAR(16) DEFAULT 'IN_PROGRESS'")
    private EnrollmentStatus status = EnrollmentStatus.IN_PROGRESS;

    @Column(nullable = false, updatable = false)
    private Instant enrolledAt;

    @Column
    private Instant completedAt;

    @PrePersist
    protected void onCreate() {
        this.enrolledAt = Instant.now();
        if (this.status == null) {
            this.status = EnrollmentStatus.IN_PROGRESS;
        }
    }
}
