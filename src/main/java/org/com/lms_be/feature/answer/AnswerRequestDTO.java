package org.com.lms_be.feature.answer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestDTO {
    @NotBlank
    @Size(max = 1000, message = "Answer length is max 1000 characters")
    private String answer;

    @NotNull(message = "Correct flag is required")
    private Boolean correct;

    @NotNull(message = "Question ID cannot be null")
    @Min(value = 1, message = "Question ID must be at least 1")
    private Long questionId;
}
