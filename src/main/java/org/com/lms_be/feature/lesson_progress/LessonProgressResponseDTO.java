package org.com.lms_be.feature.lesson_progress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class LessonProgressResponseDTO {
    private Long id;
    private Long studentId;
    private Long lessonId;
    private Long courseId;
    private boolean completed;
    private Instant completedAt;
}
