package org.com.lms_be.feature.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.com.lms_be.feature.user.UserEntity;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private UserEntity instructor;

    @Column
    private Instant updatedDate;

    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = Instant.now();   // Auto-set on first save
    }

    @PreUpdate
    protected void onUpdate() { this.updatedDate = Instant.now(); }
}
