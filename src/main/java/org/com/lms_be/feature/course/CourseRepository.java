package org.com.lms_be.feature.course;

import org.com.lms_be.util.PublishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findAllByStatusNot(PublishStatus status);

    List<CourseEntity> findAllByStatusIn(Collection<PublishStatus> statuses);
}
