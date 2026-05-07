package org.com.lms_be.feature.quiz;

import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.util.OptionalFieldDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.Optional;

@Getter
@Setter
public class QuizPatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Integer> passingScore;
}
