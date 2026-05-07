package org.com.lms_be.feature.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.quiz.QuizEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String question;

    @Column(nullable = false)
    private int sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @Column
    private Instant updatedDate;

    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
    }
}
