package org.com.lms_be.feature.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    List<AnswerEntity> findAllByQuestionId(Long questionId);

    List<AnswerEntity> findAllByQuestionIdIn(List<Long> questionIds);
}
