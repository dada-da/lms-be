package org.com.lms_be.feature.quiz_attempt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class QuizAnswerSubmissionDTO {
    @NotNull(message = "Question ID is required")
    @Min(value = 1, message = "Question ID must be at least 1")
    private Long questionId;

    private Set<Long> selectedAnswerIds = new HashSet<>();
}
