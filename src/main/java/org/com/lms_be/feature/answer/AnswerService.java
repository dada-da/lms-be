package org.com.lms_be.feature.answer;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.question.QuestionEntity;
import org.com.lms_be.feature.question.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public AnswerService(AnswerRepository answerRepository, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
    }

    @Transactional
    public AnswerResponseDTO create(AnswerRequestDTO request) {
        QuestionEntity question = questionService.getById(request.getQuestionId());

        AnswerEntity entity = new AnswerEntity();
        entity.setAnswer(request.getAnswer());
        entity.setCorrect(request.getCorrect());
        entity.setQuestion(question);

        return AnswerResponseDTO.from(answerRepository.save(entity));
    }

    public AnswerResponseDTO getResponseById(Long id) {
        return AnswerResponseDTO.from(getById(id));
    }

    public AnswerEntity getById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer", id));
    }

    public List<AnswerResponseDTO> getAllByQuestion(Long questionId) {
        questionService.getById(questionId);
        return answerRepository.findAllByQuestionId(questionId).stream()
                .map(AnswerResponseDTO::from)
                .toList();
    }

    @Transactional
    public AnswerResponseDTO updateById(Long id, AnswerPatchDTO dto) {
        AnswerEntity entity = getById(id);

        if (dto.getAnswer() != null) {
            entity.setAnswer(dto.getAnswer().orElse(null));
        }
        if (dto.getCorrect() != null) {
            dto.getCorrect().ifPresent(entity::setCorrect);
        }

        return AnswerResponseDTO.from(answerRepository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        AnswerEntity entity = getById(id);
        answerRepository.delete(entity);
    }
}
