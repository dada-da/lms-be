package org.com.lms_be.feature.quiz_attempt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.QuizAttempt;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class QuizAttemptResponseDTO {
    private Long id;
    private Long studentId;
    private Long quizId;
    private Long lessonId;
    private int score;
    private QuizAttempt status;
    private Instant attemptAt;

    static QuizAttemptResponseDTO from(QuizAttemptEntity entity) {
        return new QuizAttemptResponseDTO(
                entity.getId(),
                entity.getStudent().getId(),
                entity.getQuiz().getId(),
                entity.getQuiz().getLesson().getId(),
                entity.getScore(),
                entity.getStatus(),
                entity.getAttemptAt()
        );
    }
}
