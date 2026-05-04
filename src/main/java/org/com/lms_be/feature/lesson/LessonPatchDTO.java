package org.com.lms_be.feature.lesson;

import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.ContentType;

@Getter
@Setter
public class LessonPatchDTO {
    private String title;
    private String description;
    private Integer sequence;
    private String content;
    private ContentType contentType;
}
