package org.com.lms_be.feature.question;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequestDTO {
    @NotBlank
    @Size(max = 1000, message = "Question length is max 1000 characters")
    private String question;

    @NotNull(message = "Sequence cannot be null")
    @Min(value = 1, message = "Sequence must be at least 1")
    private Integer sequence;

    @NotNull(message = "Quiz ID cannot be null")
    @Min(value = 1, message = "Quiz ID must be at least 1")
    private Long quizId;
}
