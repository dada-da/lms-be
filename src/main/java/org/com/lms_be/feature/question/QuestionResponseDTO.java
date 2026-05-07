package org.com.lms_be.feature.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String question;
    private int sequence;
    private Long quizId;

    static QuestionResponseDTO from(QuestionEntity entity) {
        return new QuestionResponseDTO(
                entity.getId(),
                entity.getQuestion(),
                entity.getSequence(),
                entity.getQuiz().getId()
        );
    }
}
