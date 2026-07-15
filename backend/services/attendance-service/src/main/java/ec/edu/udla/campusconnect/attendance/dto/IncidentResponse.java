package ec.edu.udla.campusconnect.attendance.dto;

import ec.edu.udla.campusconnect.attendance.entity.IncidentReport;
import ec.edu.udla.campusconnect.attendance.entity.IncidentSeverity;

import java.time.OffsetDateTime;

public record IncidentResponse(
        Long id,
        String studentCode,
        IncidentSeverity severity,
        String title,
        String description,
        String reportedBy,
        OffsetDateTime reportedAt
) {
    public static IncidentResponse from(IncidentReport incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getStudentCode(),
                incident.getSeverity(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getReportedBy(),
                incident.getReportedAt()
        );
    }
}
