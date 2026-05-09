package org.com.lms_be.feature.quiz_attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttemptEntity, Long> {

    List<QuizAttemptEntity> findAllByStudentIdAndLessonIdOrderByAttemptAtDesc(Long studentId, Long lessonId);
}
