package org.com.lms_be.feature.course;

import org.com.lms_be.exception.ResourceNotFoundException;
import org.com.lms_be.feature.user.UserEntity;
import org.com.lms_be.feature.user.UserResponseDTO;
import org.com.lms_be.feature.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final UserService userService;

    public CourseService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private CourseRepository courseRepository;

    public CourseResponseDTO create(CourseRequestDTO request) {
        UserResponseDTO userResponse = this.userService.getUserById(Long.valueOf(request.getUserId())).orElseThrow(() -> new ResourceNotFoundException("User", id));;

        if (userResponse == null) {
            throw new ResourceNotFoundException("User", request.getUserId());
        }

        CourseEntity entity = new CourseEntity();
        entity.setDescription(request.getDescription());
        entity.setTitle(request.getTitle());

        UserEntity entityUser = new UserEntity();

        entity.setInstructor(entityUser);

    }
}
