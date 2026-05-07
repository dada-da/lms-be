package org.com.lms_be.feature.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerResponseDTO {
    private Long id;
    private String answer;
    private boolean correct;
    private Long questionId;

    static AnswerResponseDTO from(AnswerEntity entity) {
        return new AnswerResponseDTO(
                entity.getId(),
                entity.getAnswer(),
                entity.isCorrect(),
                entity.getQuestion().getId()
        );
    }
}
