package org.com.lms_be.feature.quiz;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.lesson.LessonEntity;
import org.com.lms_be.feature.lesson.LessonService;
import org.com.lms_be.util.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final LessonService lessonService;

    public QuizService(QuizRepository quizRepository, LessonService lessonService) {
        this.quizRepository = quizRepository;
        this.lessonService = lessonService;
    }

    @Transactional
    public QuizResponseDTO create(QuizRequestDTO request) {
        LessonEntity lesson = lessonService.getById(request.getLessonId());

        if (lesson.getContentType() != ContentType.QUIZ) {
            throw new IllegalStateException("Quiz can only be attached to a lesson of contentType=QUIZ");
        }
        if (quizRepository.existsByLessonId(lesson.getId())) {
            throw new IllegalStateException("Lesson already has a quiz");
        }

        QuizEntity entity = new QuizEntity();
        entity.setLesson(lesson);
        entity.setPassingScore(request.getPassingScore());

        return QuizResponseDTO.from(quizRepository.save(entity));
    }

    public QuizResponseDTO getResponseById(Long id) {
        return QuizResponseDTO.from(getById(id));
    }

    public QuizEntity getById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
    }

    public QuizResponseDTO getByLessonId(Long lessonId) {
        QuizEntity entity = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz for lesson " + lessonId));
        return QuizResponseDTO.from(entity);
    }

    public QuizEntity getEntityByLessonId(Long lessonId) {
        return quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz for lesson " + lessonId));
    }

    @Transactional
    public QuizResponseDTO updateById(Long id, QuizPatchDTO dto) {
        QuizEntity entity = getById(id);

        if (dto.getPassingScore() != null) {
            dto.getPassingScore().ifPresent(entity::setPassingScore);
        }

        return QuizResponseDTO.from(quizRepository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        QuizEntity entity = getById(id);
        quizRepository.delete(entity);
    }
}
