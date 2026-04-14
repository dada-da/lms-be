package org.com.lms_be.feature.lesson_progress;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.user.UserEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lesson_progress")
public class LessonProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    private boolean completed;

    private Instant completedAt;
}
