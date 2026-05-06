package org.com.lms_be.feature.lesson;

import org.com.lms_be.util.PublishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

    List<LessonEntity> findAllByStatusNot(PublishStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE LessonEntity l SET l.status = org.com.lms_be.util.PublishStatus.ARCHIVED WHERE l.course.id = :courseId")
    int archiveAllByCourseId(@Param("courseId") Long courseId);

    @Query("""
            SELECT new org.com.lms_be.feature.lesson.LessonAggregate(
                l.course.id,
                COUNT(l),
                SUM(l.durationMinutes)
            )
            FROM LessonEntity l
            WHERE l.course.id IN :courseIds
              AND l.status <> org.com.lms_be.util.PublishStatus.ARCHIVED
            GROUP BY l.course.id
            """)
    List<LessonAggregate> findAggregatesByCourseIds(@Param("courseIds") Collection<Long> courseIds);
}
