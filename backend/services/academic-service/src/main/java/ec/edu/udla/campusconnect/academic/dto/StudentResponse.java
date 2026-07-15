package ec.edu.udla.campusconnect.academic.dto;

import ec.edu.udla.campusconnect.academic.entity.Student;
import ec.edu.udla.campusconnect.academic.entity.StudentStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record StudentResponse(
        Long id,
        String studentCode,
        String documentNumber,
        String firstName,
        String lastName,
        String fullName,
        LocalDate birthDate,
        String representativeEmail,
        String representativePhone,
        StudentStatus status,
        String financialStatus,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getStudentCode(),
                student.getDocumentNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getFullName(),
                student.getBirthDate(),
                student.getRepresentativeEmail(),
                student.getRepresentativePhone(),
                student.getStatus(),
                student.getFinancialStatus(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}
