package org.com.lms_be.feature.category;

import lombok.Getter;

@Getter
public enum Category {
    PROGRAMMING("Programming"),
    DESIGN("Design"),
    BUSINESS("Business"),
    MARKETING("Marketing"),
    DATA_SCIENCE("Data Science"),
    LANGUAGE("Language"),
    MUSIC("Music"),
    PHOTOGRAPHY("Photography"),
    LIFESTYLE("Lifestyle"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
