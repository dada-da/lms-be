package org.com.lms_be.feature.quiz;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(@Valid @RequestBody QuizRequestDTO request) {
        QuizResponseDTO created = quizService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getResponseById(id));
    }

    @GetMapping
    public ResponseEntity<QuizResponseDTO> getQuizByLesson(@RequestParam Long lessonId) {
        return ResponseEntity.ok(quizService.getByLessonId(lessonId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> updateQuiz(@PathVariable Long id,
                                                      @RequestBody QuizPatchDTO dto) {
        return ResponseEntity.ok(quizService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
