package ec.edu.udla.campusconnect.attendance.service;

import ec.edu.udla.campusconnect.attendance.dto.AttendanceResponse;
import ec.edu.udla.campusconnect.attendance.dto.CreateAttendanceRequest;
import ec.edu.udla.campusconnect.attendance.dto.CreateIncidentRequest;
import ec.edu.udla.campusconnect.attendance.dto.IncidentResponse;
import ec.edu.udla.campusconnect.attendance.dto.StudentWellbeingHistoryResponse;
import ec.edu.udla.campusconnect.attendance.entity.AttendanceRecord;
import ec.edu.udla.campusconnect.attendance.entity.IncidentReport;
import ec.edu.udla.campusconnect.attendance.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.attendance.messaging.CampusEventPublisher;
import ec.edu.udla.campusconnect.attendance.repository.AttendanceRecordRepository;
import ec.edu.udla.campusconnect.attendance.repository.IncidentReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final IncidentReportRepository incidentReportRepository;
    private final CampusEventPublisher eventPublisher;

    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository,
                             IncidentReportRepository incidentReportRepository,
                             CampusEventPublisher eventPublisher) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.incidentReportRepository = incidentReportRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AttendanceResponse recordAttendance(CreateAttendanceRequest request) {
        if (attendanceRecordRepository.existsByStudentCodeAndClassDate(request.studentCode(), request.classDate())) {
            throw new BusinessRuleException("Attendance already recorded for student on this date");
        }
        AttendanceRecord record = AttendanceRecord.create(
                request.studentCode(),
                request.classDate(),
                request.type(),
                request.recordedBy(),
                request.notes()
        );
        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);
        eventPublisher.publishAttendanceRecorded(savedRecord);
        return AttendanceResponse.from(savedRecord);
    }

    @Transactional
    public IncidentResponse reportIncident(CreateIncidentRequest request) {
        IncidentReport incident = IncidentReport.create(
                request.studentCode(),
                request.severity(),
                request.title(),
                request.description(),
                request.reportedBy(),
                request.reportedAt()
        );
        IncidentReport savedIncident = incidentReportRepository.save(incident);
        eventPublisher.publishIncidentReported(savedIncident);
        return IncidentResponse.from(savedIncident);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceRecords(String studentCode) {
        List<AttendanceRecord> records = studentCode == null || studentCode.isBlank()
                ? attendanceRecordRepository.findAllByOrderByClassDateDesc()
                : attendanceRecordRepository.findByStudentCodeOrderByClassDateDesc(studentCode);
        return records.stream().map(AttendanceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> getIncidents(String studentCode) {
        List<IncidentReport> incidents = studentCode == null || studentCode.isBlank()
                ? incidentReportRepository.findAllByOrderByReportedAtDesc()
                : incidentReportRepository.findByStudentCodeOrderByReportedAtDesc(studentCode);
        return incidents.stream().map(IncidentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public StudentWellbeingHistoryResponse getStudentHistory(String studentCode) {
        return new StudentWellbeingHistoryResponse(
                studentCode,
                getAttendanceRecords(studentCode),
                getIncidents(studentCode)
        );
    }
}
