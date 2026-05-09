package org.com.lms_be.feature.quiz_attempt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class QuizAttemptResultDTO {
    private Long id;
    private Long studentId;
    private Long lessonId;
    private int correctCount;
    private int totalQuestions;
    private Instant attemptAt;

    static QuizAttemptResultDTO from(QuizAttemptEntity entity, int correctCount, int totalQuestions) {
        return new QuizAttemptResultDTO(
                entity.getId(),
                entity.getStudent().getId(),
                entity.getLesson().getId(),
                correctCount,
                totalQuestions,
                entity.getAttemptAt()
        );
    }
}
