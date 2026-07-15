package ec.edu.udla.campusconnect.attendance.dto;

import java.util.List;

public record StudentWellbeingHistoryResponse(
        String studentCode,
        List<AttendanceResponse> attendanceRecords,
        List<IncidentResponse> incidents
) {
}
