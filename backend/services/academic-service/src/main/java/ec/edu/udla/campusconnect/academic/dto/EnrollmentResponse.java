package ec.edu.udla.campusconnect.academic.dto;

import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import ec.edu.udla.campusconnect.academic.entity.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EnrollmentResponse(
        Long id,
        String studentCode,
        String schoolId,
        String grade,
        String academicYear,
        EnrollmentStatus status,
        BigDecimal pendingAmount,
        OffsetDateTime enrolledAt
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getStudentCode(),
                enrollment.getSchoolId(),
                enrollment.getGrade(),
                enrollment.getAcademicYear(),
                enrollment.getStatus(),
                enrollment.getPendingAmount(),
                enrollment.getEnrolledAt()
        );
    }
}
