package org.com.lms_be.feature.enrollment;

import jakarta.validation.Valid;
import org.com.lms_be.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> enroll(@Valid @RequestBody EnrollmentRequestDTO request,
                                                        Authentication auth) {
        EnrollmentResponseDTO created = enrollmentService.enroll(request, AuthUtil.currentUserId(auth));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> getMyEnrollments(Authentication auth) {
        return ResponseEntity.ok(enrollmentService.getAllForStudent(AuthUtil.currentUserId(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unenroll(@PathVariable Long id) {
        enrollmentService.unenroll(id);
        return ResponseEntity.noContent().build();
    }
}
