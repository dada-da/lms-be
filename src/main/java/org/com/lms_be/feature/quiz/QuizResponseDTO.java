package org.com.lms_be.feature.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuizResponseDTO {
    private Long id;
    private Long lessonId;
    private int passingScore;

    static QuizResponseDTO from(QuizEntity entity) {
        return new QuizResponseDTO(
                entity.getId(),
                entity.getLesson().getId(),
                entity.getPassingScore()
        );
    }
}
