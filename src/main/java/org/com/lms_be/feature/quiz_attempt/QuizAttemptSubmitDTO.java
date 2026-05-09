package org.com.lms_be.feature.quiz_attempt;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizAttemptSubmitDTO {
    @NotNull(message = "Lesson ID is required")
    @Min(value = 1, message = "Lesson ID must be at least 1")
    private Long lessonId;

    @Valid
    private List<QuizAnswerSubmissionDTO> answers = new ArrayList<>();
}
