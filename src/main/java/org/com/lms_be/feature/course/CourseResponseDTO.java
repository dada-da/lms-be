package org.com.lms_be.feature.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.category.Category;
import org.com.lms_be.util.PublishStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private BigDecimal price;
    private Category category;
    private Set<String> tags;
    private PublishStatus status;
    private Long lessonCount;
    private Long totalDurationMinutes;
    private Instant createdAt;
    private Instant updatedAt;
}
