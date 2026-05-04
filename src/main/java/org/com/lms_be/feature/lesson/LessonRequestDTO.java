package org.com.lms_be.feature.lesson;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.ContentType;

@Getter
@Setter
public class LessonRequestDTO {
    @NotBlank
    @Size(max= 255, message = "Title length is max 255 characters")
    private String title;

    @Size(max= 1000, message = "Description length is max 1000 characters")
    private String description;

    @NotNull(message = "Sequence cannot be null")
    @Min(value = 1, message = "Lesson sequence must be at least 1")
    private Integer sequence;

    @NotBlank
    private String content;

    @NotBlank
    private ContentType contentType = ContentType.TEXT;

    @NotNull(message = "Course ID cannot be null")
    @Min(value = 1, message = "Course ID must be at least 1")
    private Long courseId;
}
