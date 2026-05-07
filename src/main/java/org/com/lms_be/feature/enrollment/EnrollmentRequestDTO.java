package org.com.lms_be.feature.enrollment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentRequestDTO {
    @NotNull(message = "Course ID is required")
    @Min(value = 1, message = "Course ID must be at least 1")
    private Long courseId;
}
