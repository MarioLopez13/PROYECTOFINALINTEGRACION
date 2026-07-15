package ec.edu.udla.campusconnect.academic.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEnrollmentRequest(
        @NotBlank String studentCode,
        @NotBlank String schoolId,
        @NotBlank String grade,
        @NotBlank String academicYear,
        @NotNull @DecimalMin(value = "0.00") BigDecimal pendingAmount
) {
}
