package org.com.lms_be.feature.lesson_progress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgressEntity, Long> {

    Optional<LessonProgressEntity> findByStudentIdAndLessonId(Long studentId, Long lessonId);

    @Query("""
            SELECT lp FROM LessonProgressEntity lp
            WHERE lp.student.id = :studentId
              AND lp.lesson.course.id = :courseId
            """)
    List<LessonProgressEntity> findAllByStudentIdAndCourseId(@Param("studentId") Long studentId,
                                                             @Param("courseId") Long courseId);

    @Query("""
            SELECT COUNT(lp) FROM LessonProgressEntity lp
            WHERE lp.student.id = :studentId
              AND lp.lesson.course.id = :courseId
              AND lp.lesson.status = org.com.lms_be.util.PublishStatus.PUBLISHED
              AND lp.completed = true
            """)
    long countCompletedPublishedByStudentAndCourse(@Param("studentId") Long studentId,
                                                   @Param("courseId") Long courseId);
}
