package org.com.lms_be.feature.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.ContentType;

@Getter
@Setter
@AllArgsConstructor
public class LessonResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer sequence;
    private Integer durationMinutes;
    private String content;
    private ContentType contentType;

    LessonResponseDTO(LessonEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.sequence = entity.getSequence();
        this.durationMinutes = entity.getDurationMinutes();
        this.content = entity.getContent();
        this.contentType = entity.getContentType();
    }
}
