package org.com.lms_be.feature.question;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.quiz.QuizEntity;
import org.com.lms_be.feature.quiz.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizService quizService;

    public QuestionService(QuestionRepository questionRepository, QuizService quizService) {
        this.questionRepository = questionRepository;
        this.quizService = quizService;
    }

    @Transactional
    public QuestionResponseDTO create(QuestionRequestDTO request) {
        QuizEntity quiz = quizService.getById(request.getQuizId());

        QuestionEntity entity = new QuestionEntity();
        entity.setQuestion(request.getQuestion());
        entity.setSequence(request.getSequence());
        entity.setQuiz(quiz);

        return QuestionResponseDTO.from(questionRepository.save(entity));
    }

    public QuestionResponseDTO getResponseById(Long id) {
        return QuestionResponseDTO.from(getById(id));
    }

    public QuestionEntity getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));
    }

    public List<QuestionResponseDTO> getAllByQuiz(Long quizId) {
        quizService.getById(quizId);
        return questionRepository.findAllByQuizIdOrderBySequenceAsc(quizId).stream()
                .map(QuestionResponseDTO::from)
                .toList();
    }

    @Transactional
    public QuestionResponseDTO updateById(Long id, QuestionPatchDTO dto) {
        QuestionEntity entity = getById(id);

        if (dto.getQuestion() != null) {
            entity.setQuestion(dto.getQuestion().orElse(null));
        }
        if (dto.getSequence() != null) {
            dto.getSequence().ifPresent(entity::setSequence);
        }

        return QuestionResponseDTO.from(questionRepository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        QuestionEntity entity = getById(id);
        questionRepository.delete(entity);
    }
}
