package org.com.lms_be.feature.quiz_attempt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.quiz.QuizEntity;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "quiz_attempt")
public class QuizAttemptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<QuizEntity> quiz;

    private int score;

    private int totalMarks;

    private Instant attemptAt;
}
