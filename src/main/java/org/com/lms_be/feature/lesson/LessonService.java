package org.com.lms_be.feature.lesson;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.course.CourseService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class LessonService {
    private final CourseService courseService;
    private final LessonRepository lessonRepository;

    public LessonService(CourseService courseService, LessonRepository lessonRepository) {
        this.courseService = courseService;
        this.lessonRepository = lessonRepository;
    }

    public LessonResponseDTO create(LessonRequestDTO request) {
        CourseEntity courseResponse = this.courseService.getById(request.getCourseId());

        LessonEntity lessonEntity = new LessonEntity();

        lessonEntity.setTitle(request.getTitle());
        lessonEntity.setDescription(request.getDescription());
        lessonEntity.setCourse(courseResponse);
        lessonEntity.setContent(request.getContent());
        lessonEntity.setContentType(request.getContentType());
        lessonEntity.setSequence(request.getSequence());

        return toResponseDTO(lessonRepository.save(lessonEntity));
    }

    public LessonResponseDTO updateById(Long id, Map<String, Object> fields) {
        LessonEntity lessonEntity = this.getById(id);

        fields.forEach((key, value) -> {
            Field field = null;

            try {
                field = lessonEntity.getClass().getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

            try {
                field.set(lessonEntity, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return toResponseDTO(lessonEntity);
    }

    public LessonEntity getById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", id));
    }

    public List<LessonResponseDTO> getAll() {
        return lessonRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public void deleteById(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson", id);
        }

        lessonRepository.deleteById(id);
    }

    private LessonResponseDTO toResponseDTO(LessonEntity entity) {
        return new LessonResponseDTO(entity);
    }
}
