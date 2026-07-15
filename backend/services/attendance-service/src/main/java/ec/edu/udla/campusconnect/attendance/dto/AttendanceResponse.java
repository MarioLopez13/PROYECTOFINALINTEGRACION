package ec.edu.udla.campusconnect.attendance.dto;

import ec.edu.udla.campusconnect.attendance.entity.AttendanceRecord;
import ec.edu.udla.campusconnect.attendance.entity.AttendanceType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AttendanceResponse(
        Long id,
        String studentCode,
        LocalDate classDate,
        AttendanceType type,
        String recordedBy,
        String notes,
        OffsetDateTime recordedAt
) {
    public static AttendanceResponse from(AttendanceRecord record) {
        return new AttendanceResponse(
                record.getId(),
                record.getStudentCode(),
                record.getClassDate(),
                record.getType(),
                record.getRecordedBy(),
                record.getNotes(),
                record.getRecordedAt()
        );
    }
}
