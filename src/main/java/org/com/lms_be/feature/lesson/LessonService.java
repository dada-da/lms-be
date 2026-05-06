package org.com.lms_be.feature.lesson;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.util.PublishStatus;
import org.springframework.stereotype.Service;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.course.CourseService;

import java.util.List;

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
        lessonEntity.setDurationMinutes(request.getDurationMinutes());

        return toResponseDTO(lessonRepository.save(lessonEntity));
    }

    public LessonResponseDTO updateById(Long id, LessonPatchDTO dto) {
        LessonEntity entity = this.getById(id);

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle().orElse(null));
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription().orElse(null));
        }
        if (dto.getSequence() != null) {
            entity.setSequence(dto.getSequence().orElse(0));
        }
        if (dto.getContent() != null) {
            entity.setContent(dto.getContent().orElse(null));
        }
        if (dto.getContentType() != null) {
            entity.setContentType(dto.getContentType().orElse(null));
        }
        if (dto.getDurationMinutes() != null) {
            entity.setDurationMinutes(dto.getDurationMinutes().orElse(null));
        }
        if (dto.getStatus() != null) {
            dto.getStatus().ifPresent(entity::setStatus);
        }

        return toResponseDTO(lessonRepository.save(entity));
    }

    public LessonEntity getById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", id));
    }

    public List<LessonResponseDTO> getAll() {
        return lessonRepository.findAllByStatusNot(PublishStatus.ARCHIVED).stream()
                .map(this::toResponseDTO).toList();
    }

    public void deleteById(Long id) {
        LessonEntity entity = getById(id);
        entity.setStatus(PublishStatus.ARCHIVED);
        lessonRepository.save(entity);
    }

    private LessonResponseDTO toResponseDTO(LessonEntity entity) {
        return new LessonResponseDTO(entity);
    }
}
