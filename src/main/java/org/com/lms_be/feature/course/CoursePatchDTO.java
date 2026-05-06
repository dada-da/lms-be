package org.com.lms_be.feature.course;

import tools.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.OptionalFieldDeserializer;

import java.util.Optional;

@Getter
@Setter
public class CoursePatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> title;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> description;
}
