package ec.edu.udla.campusconnect.attendance.repository;

import ec.edu.udla.campusconnect.attendance.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByStudentCodeOrderByReportedAtDesc(String studentCode);

    List<IncidentReport> findAllByOrderByReportedAtDesc();
}
