package org.com.lms_be.feature.question;

import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.OptionalFieldDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.Optional;

@Getter
@Setter
public class QuestionPatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> question;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Integer> sequence;
}
