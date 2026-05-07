package org.com.lms_be.feature.answer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<AnswerResponseDTO> createAnswer(@Valid @RequestBody AnswerRequestDTO request) {
        AnswerResponseDTO created = answerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> getAnswer(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getResponseById(id));
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswers(@RequestParam Long questionId) {
        return ResponseEntity.ok(answerService.getAllByQuestion(questionId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(@PathVariable Long id,
                                                          @RequestBody AnswerPatchDTO dto) {
        return ResponseEntity.ok(answerService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
