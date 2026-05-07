package org.com.lms_be.feature.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

    Optional<QuizEntity> findByLessonId(Long lessonId);

    boolean existsByLessonId(Long lessonId);
}
