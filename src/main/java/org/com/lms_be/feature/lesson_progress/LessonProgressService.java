package org.com.lms_be.feature.lesson_progress;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.enrollment.EnrollmentRepository;
import org.com.lms_be.feature.enrollment.EnrollmentService;
import org.com.lms_be.feature.lesson.LessonEntity;
import org.com.lms_be.feature.lesson.LessonRepository;
import org.com.lms_be.feature.lesson.LessonService;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserRepository;
import org.com.lms_be.util.PublishStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final LessonService lessonService;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository,
                                 LessonRepository lessonRepository,
                                 LessonService lessonService,
                                 UserRepository userRepository,
                                 EnrollmentRepository enrollmentRepository,
                                 EnrollmentService enrollmentService) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.lessonService = lessonService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentService = enrollmentService;
    }

    @Transactional
    public LessonProgressResponseDTO upsert(LessonProgressRequestDTO request) {
        UserEntity student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getStudentId()));

        LessonEntity lesson = lessonService.getById(request.getLessonId());
        if (lesson.getStatus() != PublishStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot track progress on a lesson that is not published");
        }

        Long courseId = lesson.getCourse().getId();
        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("Student is not enrolled in the course this lesson belongs to");
        }

        LessonProgressEntity entity = lessonProgressRepository
                .findByStudentIdAndLessonId(student.getId(), lesson.getId())
                .orElseGet(() -> {
                    LessonProgressEntity fresh = new LessonProgressEntity();
                    fresh.setStudent(student);
                    fresh.setLesson(lesson);
                    return fresh;
                });

        entity.setCompleted(request.getCompleted());
        entity.setCompletedAt(request.getCompleted() ? Instant.now() : null);

        LessonProgressEntity saved = lessonProgressRepository.save(entity);

        long completedCount = lessonProgressRepository
                .countCompletedPublishedByStudentAndCourse(student.getId(), courseId);
        long publishedCount = lessonRepository.countByCourseIdAndStatus(courseId, PublishStatus.PUBLISHED);
        enrollmentService.recomputeStatus(student.getId(), courseId, completedCount, publishedCount);

        return toResponseDTO(saved);
    }

    public List<LessonProgressResponseDTO> getAll(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("Both studentId and courseId are required");
        }
        return lessonProgressRepository.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private LessonProgressResponseDTO toResponseDTO(LessonProgressEntity entity) {
        LessonEntity lesson = entity.getLesson();
        return new LessonProgressResponseDTO(
                entity.getId(),
                entity.getStudent().getId(),
                lesson.getId(),
                lesson.getCourse().getId(),
                entity.isCompleted(),
                entity.getCompletedAt()
        );
    }
}
