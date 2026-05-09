package org.com.lms_be.feature.quiz_attempt;

import jakarta.validation.Valid;
import org.com.lms_be.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-attempt")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping
    public ResponseEntity<QuizAttemptResultDTO> submit(@Valid @RequestBody QuizAttemptSubmitDTO request,
                                                       Authentication auth) {
        QuizAttemptResultDTO created = quizAttemptService.submit(request, AuthUtil.currentUserId(auth));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<QuizAttemptResponseDTO>> getMyAttemptsForLesson(@RequestParam Long lessonId,
                                                                               Authentication auth) {
        return ResponseEntity.ok(quizAttemptService.getMyAttemptsForLesson(AuthUtil.currentUserId(auth), lessonId));
    }
}
