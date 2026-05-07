package org.com.lms_be.feature.quiz_attempt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.quiz.QuizEntity;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.util.QuizAttempt;

import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "quiz_attempt")
public class QuizAttemptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private QuizAttempt status;

    @Column(nullable = false, updatable = false)
    private Instant attemptAt;

    @PrePersist
    protected void onCreate() {
        this.attemptAt = Instant.now();
    }
}
