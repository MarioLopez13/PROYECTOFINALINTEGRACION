package ec.edu.udla.campusconnect.attendance.dto;

import ec.edu.udla.campusconnect.attendance.entity.AttendanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAttendanceRequest(
        @NotBlank String studentCode,
        @NotNull LocalDate classDate,
        @NotNull AttendanceType type,
        @NotBlank String recordedBy,
        String notes
) {
}
