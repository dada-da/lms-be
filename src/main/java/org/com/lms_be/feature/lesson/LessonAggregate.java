package org.com.lms_be.feature.lesson;

public record LessonAggregate(Long courseId, Long lessonCount, Long totalDurationMinutes) {
}
