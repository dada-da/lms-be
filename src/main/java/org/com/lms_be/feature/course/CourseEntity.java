package org.com.lms_be.feature.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.category.Category;
import org.com.lms_be.feature.user.UserEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private Category category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "course_tags", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "tag", length = 64)
    private Set<String> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private UserEntity instructor;

    @Column
    private Instant updatedDate;

    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Instant.now();
    }
}
