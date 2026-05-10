package org.com.lms_be.feature.lesson;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.util.ContentType;
import org.com.lms_be.util.HtmlSanitizer;
import org.com.lms_be.util.PublishStatus;
import org.com.lms_be.util.VideoContentValidator;
import org.springframework.stereotype.Service;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.course.CourseService;

import java.util.List;

@Service
public class LessonService {
    private final CourseService courseService;
    private final LessonRepository lessonRepository;
    private final HtmlSanitizer htmlSanitizer;
    private final VideoContentValidator videoContentValidator;

    public LessonService(CourseService courseService,
                         LessonRepository lessonRepository,
                         HtmlSanitizer htmlSanitizer,
                         VideoContentValidator videoContentValidator) {
        this.courseService = courseService;
        this.lessonRepository = lessonRepository;
        this.htmlSanitizer = htmlSanitizer;
        this.videoContentValidator = videoContentValidator;
    }

    public LessonResponseDTO create(LessonRequestDTO request) {
        CourseEntity courseResponse = this.courseService.getById(request.getCourseId());

        LessonEntity lessonEntity = new LessonEntity();

        lessonEntity.setTitle(request.getTitle());
        lessonEntity.setDescription(request.getDescription());
        lessonEntity.setCourse(courseResponse);
        lessonEntity.setContentType(request.getContentType());
        lessonEntity.setContent(request.getContent());
        lessonEntity.setSequence(request.getSequence());
        lessonEntity.setDurationMinutes(request.getDurationMinutes());

        applyContentRule(lessonEntity);

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

        applyContentRule(entity);

        return toResponseDTO(lessonRepository.save(entity));
    }

    public LessonEntity getById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", id));
    }

    public List<LessonResponseDTO> getAllByCourse(Long courseId, List<PublishStatus> visibleStatuses) {
        courseService.getById(courseId);
        return lessonRepository.findAllByCourseIdAndStatusIn(courseId, visibleStatuses).stream()
                .map(this::toResponseDTO).toList();
    }

    public void deleteById(Long id) {
        LessonEntity entity = getById(id);
        entity.setStatus(PublishStatus.ARCHIVED);
        lessonRepository.save(entity);
    }

    private void applyContentRule(LessonEntity entity) {
        if (entity.getContentType() == ContentType.QUIZ) {
            entity.setContent(null);
            return;
        }
        if (entity.getContent() == null || entity.getContent().isBlank()) {
            throw new IllegalArgumentException("Content is required for non-quiz lessons");
        }
        if (entity.getContentType() == ContentType.TEXT) {
            videoContentValidator.rejectVideoTags(entity.getContent());
            String sanitized = htmlSanitizer.sanitize(entity.getContent());
            videoContentValidator.rejectVideoUrls(sanitized);
            entity.setContent(sanitized);
        }
    }

    private LessonResponseDTO toResponseDTO(LessonEntity entity) {
        return new LessonResponseDTO(entity);
    }
}
