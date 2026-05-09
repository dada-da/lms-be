package org.com.lms_be.feature.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    List<QuestionEntity> findAllByLessonIdOrderBySequenceAsc(Long lessonId);

    long countByLessonId(Long lessonId);
}
