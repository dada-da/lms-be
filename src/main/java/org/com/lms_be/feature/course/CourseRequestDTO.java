package org.com.lms_be.feature.course;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.category.Category;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CourseRequestDTO {
    @NotBlank
    @Size(max = 255, message = "Title length is max 255 characters")
    private String title;

    @Size(max = 1000, message = "Description length is max 1000 characters")
    private String description;

    @URL(message = "Thumbnail must be a valid URL")
    @Size(max = 1000)
    private String thumbnailUrl;

    @DecimalMin(value = "0.0", message = "Price must be 0 or greater")
    private BigDecimal price;

    @NotNull(message = "Category is required")
    private Category category;

    @Size(max = 10, message = "Maximum 10 tags allowed")
    private Set<@Size(max = 64) String> tags = new HashSet<>();

    @NotNull(message = "ID cannot be null")
    @Min(value = 1, message = "ID must be at least 1")
    private Long userId;
}
