package org.com.lms_be.feature.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface LessonRepository extends JpaRepository<LessonEntity, Long> {

    @Query("""
            SELECT new org.com.lms_be.feature.lesson.LessonAggregate(
                l.course.id,
                COUNT(l),
                SUM(l.durationMinutes)
            )
            FROM LessonEntity l
            WHERE l.course.id IN :courseIds
            GROUP BY l.course.id
            """)
    List<LessonAggregate> findAggregatesByCourseIds(@Param("courseIds") Collection<Long> courseIds);
}
