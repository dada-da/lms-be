package org.com.lms_be.feature.lesson;

import org.com.lms_be.feature.course.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<CourseEntity, Long> {
}
