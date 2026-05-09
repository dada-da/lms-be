package org.com.lms_be.feature.question;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.lesson.LessonEntity;
import org.com.lms_be.feature.lesson.LessonService;
import org.com.lms_be.util.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LessonService lessonService;

    public QuestionService(QuestionRepository questionRepository, LessonService lessonService) {
        this.questionRepository = questionRepository;
        this.lessonService = lessonService;
    }

    @Transactional
    public QuestionResponseDTO create(QuestionRequestDTO request) {
        LessonEntity lesson = lessonService.getById(request.getLessonId());
        if (lesson.getContentType() != ContentType.QUIZ) {
            throw new IllegalStateException("Questions can only be attached to a lesson of contentType=QUIZ");
        }

        QuestionEntity entity = new QuestionEntity();
        entity.setQuestion(request.getQuestion());
        entity.setSequence(request.getSequence());
        entity.setLesson(lesson);

        return QuestionResponseDTO.from(questionRepository.save(entity));
    }

    public QuestionResponseDTO getResponseById(Long id) {
        return QuestionResponseDTO.from(getById(id));
    }

    public QuestionEntity getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));
    }

    public List<QuestionResponseDTO> getAllByLesson(Long lessonId) {
        lessonService.getById(lessonId);
        return questionRepository.findAllByLessonIdOrderBySequenceAsc(lessonId).stream()
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
