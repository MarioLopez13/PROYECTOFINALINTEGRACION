package ec.edu.udla.campusconnect.academic.repository;

import ec.edu.udla.campusconnect.academic.entity.StudentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentEventRepository extends JpaRepository<StudentEvent, Long> {
    boolean existsBySourceEventId(String sourceEventId);

    List<StudentEvent> findByStudentCodeOrderByOccurredAtDesc(String studentCode);
}
