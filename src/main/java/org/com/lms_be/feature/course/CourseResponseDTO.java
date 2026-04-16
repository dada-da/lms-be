package org.com.lms_be.feature.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponseDTO {
    private String id;
    private String title;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
