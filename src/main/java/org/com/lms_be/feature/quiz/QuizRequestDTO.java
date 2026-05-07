package org.com.lms_be.feature.quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizRequestDTO {
    @NotNull(message = "Lesson ID cannot be null")
    @Min(value = 1, message = "Lesson ID must be at least 1")
    private Long lessonId;

    @NotNull(message = "Passing score is required")
    @Min(value = 0, message = "Passing score must be at least 0")
    @Max(value = 100, message = "Passing score must be at most 100")
    private Integer passingScore;
}
