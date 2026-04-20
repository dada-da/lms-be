package org.com.lms_be.feature.course;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserResponseDTO;
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
        UserResponseDTO userResponse = this.userService.getUserById(request.getUserId());

        if (userResponse == null) {
            throw new ResourceNotFoundException("User", request.getUserId());
        }

        CourseEntity entity = new CourseEntity();
        entity.setDescription(request.getDescription());
        entity.setTitle(request.getTitle());

        UserEntity entityUser = new UserEntity();

        entity.setInstructor(entityUser);

        return toResponseDTO(courseRepository.save(entity));
    }

    public CourseResponseDTO getById(Long id) {
        CourseEntity course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return toResponseDTO(course);
    }

    public List<CourseResponseDTO> getAll() {
        return courseRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public CourseResponseDTO updateById(Long id, CourseRequestDTO request) {
        UserResponseDTO userResponse = this.userService.getUserById(request.getUserId());
        CourseResponseDTO response = this.getById(id);

        if (userResponse == null) {
            throw new ResourceNotFoundException("User", request.getUserId());
        }

        if (response == null) {
            throw new ResourceNotFoundException("Course", id);
        }

        CourseEntity entity = new CourseEntity();

        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
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
