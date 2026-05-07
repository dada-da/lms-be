package org.com.lms_be.feature.quiz;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.lesson.LessonEntity;

@Getter
@Setter
@Entity
@Table(name = "quiz", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lesson_id"})
})
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonEntity lesson;

    @Column(name = "passing_score", nullable = false)
    private int passingScore;
}
