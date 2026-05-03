package org.com.lms_be.feature.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDTO {
    @NotBlank
    @Size(max= 255, message = "Title length is max 255 characters")
    private String title;

    @Size(max= 1000, message = "Description length is max 1000 characters")
    private String description;

    @NotNull(message = "ID cannot be null")
    @Min(value = 1, message = "ID must be at least 1")
    private Long userId;
}
