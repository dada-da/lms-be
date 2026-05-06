package org.com.lms_be.feature.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonResponseDTO> createLesson(@RequestBody LessonRequestDTO lessonRequestDTO) {
        LessonResponseDTO created = lessonService.create(lessonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("{id}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable Long id, @RequestBody LessonPatchDTO lessonPatchDTO) {
        return new ResponseEntity<>(lessonService.updateById(id, lessonPatchDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonEntity> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAll());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<LessonResponseDTO> deleteLesson(@PathVariable Long id) {
        lessonService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
