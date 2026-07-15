package ec.edu.udla.campusconnect.academic.repository;

import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentStudentCodeAndAcademicYear(String studentCode, String academicYear);

    List<Enrollment> findByStudentStudentCode(String studentCode);
}
