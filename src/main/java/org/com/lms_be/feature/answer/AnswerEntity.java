package org.com.lms_be.feature.answer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.question.QuestionEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="answer")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String answer;

    @Column
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Column
    private Instant updatedDate;

    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = Instant.now();   // Auto-set on first save
    }

    @PreUpdate
    protected void onUpdate() { this.updatedDate = Instant.now(); }
}
