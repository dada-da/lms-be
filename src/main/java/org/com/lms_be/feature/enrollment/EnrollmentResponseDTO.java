package org.com.lms_be.feature.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.EnrollmentStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EnrollmentResponseDTO {
    private Long id;
    private Long studentId;
    private String studentEmail;
    private Long courseId;
    private String courseTitle;
    private EnrollmentStatus status;
    private Instant enrolledAt;
    private Instant completedAt;
}
