package org.com.lms_be.feature.lesson_progress;

import jakarta.validation.Valid;
import org.com.lms_be.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<LessonProgressResponseDTO> upsert(@Valid @RequestBody LessonProgressRequestDTO request,
                                                            Authentication auth) {
        return ResponseEntity.ok(lessonProgressService.upsert(request, AuthUtil.currentUserId(auth)));
    }

    @GetMapping
    public ResponseEntity<List<LessonProgressResponseDTO>> getAll(@RequestParam Long courseId,
                                                                  Authentication auth) {
        return ResponseEntity.ok(lessonProgressService.getAllForStudent(AuthUtil.currentUserId(auth), courseId));
    }
}
