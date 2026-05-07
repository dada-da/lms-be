package org.com.lms_be.feature.course;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.lesson.LessonAggregate;
import org.com.lms_be.feature.lesson.LessonRepository;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserService;
import org.com.lms_be.util.PublishStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseService(UserService userService, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    public CourseResponseDTO create(CourseRequestDTO request, Long instructorId) {
        UserEntity userResponse = this.userService.getVerifiedUserReference(instructorId);

        CourseEntity entity = new CourseEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setThumbnailUrl(request.getThumbnailUrl());
        entity.setPrice(request.getPrice());
        entity.setCategory(request.getCategory());
        entity.setTags(request.getTags() != null ? new HashSet<>(request.getTags()) : new HashSet<>());
        entity.setInstructor(userResponse);

        CourseEntity saved = courseRepository.save(entity);
        return toResponseDTO(saved, emptyAggregate(saved.getId()));
    }

    public CourseEntity getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    public CourseResponseDTO getResponseById(Long id) {
        CourseEntity entity = getById(id);
        LessonAggregate agg = lessonRepository.findAggregatesByCourseIds(List.of(id))
                .stream().findFirst().orElseGet(() -> emptyAggregate(id));
        return toResponseDTO(entity, agg);
    }

    public List<CourseResponseDTO> getAll(List<PublishStatus> visibleStatuses) {
        List<CourseEntity> courses = courseRepository.findAllByStatusIn(visibleStatuses);
        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = courses.stream().map(CourseEntity::getId).toList();
        Map<Long, LessonAggregate> stats = lessonRepository.findAggregatesByCourseIds(ids).stream()
                .collect(Collectors.toMap(LessonAggregate::courseId, Function.identity()));

        return courses.stream()
                .map(c -> toResponseDTO(c, stats.getOrDefault(c.getId(), emptyAggregate(c.getId()))))
                .toList();
    }

    public CourseResponseDTO updateById(Long id, CoursePatchDTO dto) {
        CourseEntity entity = this.getById(id);

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle().orElse(null));
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription().orElse(null));
        }
        if (dto.getThumbnailUrl() != null) {
            entity.setThumbnailUrl(dto.getThumbnailUrl().orElse(null));
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice().orElse(null));
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory().orElse(null));
        }
        if (dto.getTags() != null) {
            entity.setTags(dto.getTags().map(HashSet::new).orElseGet(HashSet::new));
        }
        if (dto.getStatus() != null) {
            dto.getStatus().ifPresent(entity::setStatus);
        }

        CourseEntity saved = courseRepository.save(entity);
        LessonAggregate agg = lessonRepository.findAggregatesByCourseIds(List.of(id))
                .stream().findFirst().orElseGet(() -> emptyAggregate(id));
        return toResponseDTO(saved, agg);
    }

    @Transactional
    public void deleteById(Long id) {
        CourseEntity entity = getById(id);
        entity.setStatus(PublishStatus.ARCHIVED);
        courseRepository.save(entity);
        lessonRepository.archiveAllByCourseId(id);
    }

    private CourseResponseDTO toResponseDTO(CourseEntity entity, LessonAggregate agg) {
        return new CourseResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getThumbnailUrl(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getTags(),
                entity.getStatus(),
                agg.lessonCount() != null ? agg.lessonCount() : 0L,
                agg.totalDurationMinutes() != null ? agg.totalDurationMinutes() : 0L,
                entity.getCreatedDate(),
                entity.getUpdatedDate()
        );
    }

    private LessonAggregate emptyAggregate(Long courseId) {
        return new LessonAggregate(courseId, 0L, 0L);
    }
}
