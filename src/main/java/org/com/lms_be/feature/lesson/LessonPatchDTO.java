package org.com.lms_be.feature.lesson;

import tools.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.ContentType;
import org.com.lms_be.util.OptionalFieldDeserializer;

import java.util.Optional;

@Getter
@Setter
public class LessonPatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> title;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> description;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Integer> sequence;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Integer> durationMinutes;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> content;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<ContentType> contentType;
}
