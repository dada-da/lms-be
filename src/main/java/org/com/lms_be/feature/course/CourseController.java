package org.com.lms_be.feature.course;

import org.com.lms_be.util.PublishStatus;
import org.com.lms_be.util.PublishStatusAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO created = courseService.create(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getResponseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(
            @RequestParam(required = false) PublishStatus status,
            Authentication auth) {
        List<PublishStatus> visible = PublishStatusAccess.resolveVisibleStatuses(status, auth);
        return ResponseEntity.ok(courseService.getAll(visible));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @RequestBody CoursePatchDTO coursePatchDTO) {
        return new ResponseEntity<>(courseService.updateById(id, coursePatchDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
