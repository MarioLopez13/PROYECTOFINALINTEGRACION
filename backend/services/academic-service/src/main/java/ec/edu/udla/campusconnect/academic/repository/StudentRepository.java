package ec.edu.udla.campusconnect.academic.repository;

import ec.edu.udla.campusconnect.academic.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByStudentCode(String studentCode);

    Optional<Student> findByStudentCode(String studentCode);
}
