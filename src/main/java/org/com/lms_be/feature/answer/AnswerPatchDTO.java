package org.com.lms_be.feature.answer;

import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.OptionalFieldDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.Optional;

@Getter
@Setter
public class AnswerPatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> answer;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Boolean> correct;
}
