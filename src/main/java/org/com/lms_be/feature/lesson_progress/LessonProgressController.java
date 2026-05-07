package org.com.lms_be.feature.lesson_progress;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson-progress")
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;

    public LessonProgressController(LessonProgressService lessonProgressService) {
        this.lessonProgressService = lessonProgressService;
    }

    @PutMapping
    public ResponseEntity<LessonProgressResponseDTO> upsert(@Valid @RequestBody LessonProgressRequestDTO request) {
        return ResponseEntity.ok(lessonProgressService.upsert(request));
    }

    @GetMapping
    public ResponseEntity<List<LessonProgressResponseDTO>> getAll(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(lessonProgressService.getAll(studentId, courseId));
    }
}
