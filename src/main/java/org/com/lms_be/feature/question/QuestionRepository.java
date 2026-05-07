package org.com.lms_be.feature.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    List<QuestionEntity> findAllByQuizIdOrderBySequenceAsc(Long quizId);

    long countByQuizId(Long quizId);
}
