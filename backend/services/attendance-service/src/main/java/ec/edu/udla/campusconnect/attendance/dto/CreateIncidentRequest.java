package ec.edu.udla.campusconnect.attendance.dto;

import ec.edu.udla.campusconnect.attendance.entity.IncidentSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateIncidentRequest(
        @NotBlank String studentCode,
        @NotNull IncidentSeverity severity,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String reportedBy,
        OffsetDateTime reportedAt
) {
}
