package org.com.lms_be.feature.course;

import tools.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.category.Category;
import org.com.lms_be.util.OptionalFieldDeserializer;
import org.com.lms_be.util.PublishStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class CoursePatchDTO {
    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> title;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> description;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<String> thumbnailUrl;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<BigDecimal> price;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Category> category;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<Set<String>> tags;

    @JsonDeserialize(using = OptionalFieldDeserializer.class)
    private Optional<PublishStatus> status;
}
