package org.com.lms_be.feature.enrollment;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.course.CourseEntity;
import org.com.lms_be.feature.course.CourseService;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserRepository;
import org.com.lms_be.util.EnrollmentStatus;
import org.com.lms_be.util.PublishStatus;
import org.com.lms_be.util.UserRole;

import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    @Transactional
    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO request, Long studentId) {
        UserEntity student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", studentId));

        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("Only users with STUDENT role can enroll in a course");
        }

        CourseEntity course = courseService.getById(request.getCourseId());
        if (course.getStatus() != PublishStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot enroll in a course that is not published");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        EnrollmentEntity entity = new EnrollmentEntity();
        entity.setStudent(student);
        entity.setCourse(course);

        try {
            EnrollmentEntity saved = enrollmentRepository.save(entity);
            return toResponseDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }
    }

    public EnrollmentResponseDTO getById(Long id) {
        return toResponseDTO(getEntityById(id));
    }

    public List<EnrollmentResponseDTO> getAllForStudent(Long studentId) {
        return enrollmentRepository.findAllByStudentId(studentId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void unenroll(Long id) {
        EnrollmentEntity entity = getEntityById(id);
        enrollmentRepository.delete(entity);
    }

    @Transactional
    public void recomputeStatus(Long studentId, Long courseId, long completedLessonCount, long publishedLessonCount) {
        EnrollmentEntity entity = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found for student " + studentId + " in course " + courseId));

        boolean shouldBeComplete = publishedLessonCount > 0 && completedLessonCount >= publishedLessonCount;

        if (shouldBeComplete && entity.getStatus() != EnrollmentStatus.COMPLETED) {
            entity.setStatus(EnrollmentStatus.COMPLETED);
            entity.setCompletedAt(Instant.now());
            enrollmentRepository.save(entity);
        } else if (!shouldBeComplete && entity.getStatus() == EnrollmentStatus.COMPLETED) {
            entity.setStatus(EnrollmentStatus.IN_PROGRESS);
            entity.setCompletedAt(null);
            enrollmentRepository.save(entity);
        }
    }

    public EnrollmentEntity getEntityByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found for student " + studentId + " in course " + courseId));
    }

    private EnrollmentEntity getEntityById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
    }

    private EnrollmentResponseDTO toResponseDTO(EnrollmentEntity entity) {
        UserEntity student = entity.getStudent();
        CourseEntity course = entity.getCourse();
        return new EnrollmentResponseDTO(
                entity.getId(),
                student.getId(),
                student.getEmail(),
                course.getId(),
                course.getTitle(),
                entity.getStatus(),
                entity.getEnrolledAt(),
                entity.getCompletedAt()
        );
    }
}
