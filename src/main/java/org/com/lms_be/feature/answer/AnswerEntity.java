package org.com.lms_be.feature.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.question.QuestionEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "answer")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String answer;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

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
