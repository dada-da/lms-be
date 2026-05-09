package org.com.lms_be.feature.quiz_attempt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.lesson.LessonEntity;
import org.com.lms_be.feature.user.UserEntity;

import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "quiz_attempt")
public class QuizAttemptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonEntity lesson;

    @Column(nullable = false, updatable = false)
    private Instant attemptAt;

    @PrePersist
    protected void onCreate() {
        this.attemptAt = Instant.now();
    }
}
