package org.com.lms_be.feature.lesson_progress;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonProgressRequestDTO {
    @NotNull(message = "Student ID is required")
    @Min(value = 1, message = "Student ID must be at least 1")
    private Long studentId;

    @NotNull(message = "Lesson ID is required")
    @Min(value = 1, message = "Lesson ID must be at least 1")
    private Long lessonId;

    @NotNull(message = "Completed flag is required")
    private Boolean completed;
}
