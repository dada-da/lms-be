package org.com.lms_be.feature.course;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final UserService userService;
    private final CourseRepository courseRepository;

    public CourseService(UserService userService, CourseRepository courseRepository) {
        this.userService = userService;
        this.courseRepository = courseRepository;
    }

    public CourseResponseDTO create(CourseRequestDTO request) {
        UserEntity userResponse = this.userService.getVerifiedUserReference(request.getUserId());

        CourseEntity entity = new CourseEntity();
        entity.setDescription(request.getDescription());
        entity.setTitle(request.getTitle());

        entity.setInstructor(userResponse);

        return toResponseDTO(courseRepository.save(entity));
    }

    public CourseEntity getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    public List<CourseResponseDTO> getAll() {
        return courseRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public CourseResponseDTO updateById(Long id, CoursePatchDTO dto) {
        CourseEntity entity = this.getById(id);

        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle().orElse(null));
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription().orElse(null));
        }

        return toResponseDTO(courseRepository.save(entity));
    }

    public void deleteById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", id);
        }

        courseRepository.deleteById(id);
    }

    private CourseResponseDTO toResponseDTO(CourseEntity entity) {
        return new CourseResponseDTO(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getCreatedDate(), entity.getUpdatedDate());
    }
}
