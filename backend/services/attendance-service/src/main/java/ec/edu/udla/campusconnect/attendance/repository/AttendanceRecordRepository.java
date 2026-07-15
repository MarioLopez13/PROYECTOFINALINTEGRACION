package ec.edu.udla.campusconnect.attendance.repository;

import ec.edu.udla.campusconnect.attendance.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    boolean existsByStudentCodeAndClassDate(String studentCode, LocalDate classDate);

    List<AttendanceRecord> findByStudentCodeOrderByClassDateDesc(String studentCode);

    List<AttendanceRecord> findAllByOrderByClassDateDesc();
}
