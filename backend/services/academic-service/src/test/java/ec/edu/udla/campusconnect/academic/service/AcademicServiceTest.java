package ec.edu.udla.campusconnect.academic.service;

import ec.edu.udla.campusconnect.academic.dto.CreateEnrollmentRequest;
import ec.edu.udla.campusconnect.academic.dto.CreateStudentRequest;
import ec.edu.udla.campusconnect.academic.dto.EnrollmentResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentResponse;
import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import ec.edu.udla.campusconnect.academic.entity.EnrollmentStatus;
import ec.edu.udla.campusconnect.academic.entity.Student;
import ec.edu.udla.campusconnect.academic.entity.StudentStatus;
import ec.edu.udla.campusconnect.academic.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.academic.exception.ResourceNotFoundException;
import ec.edu.udla.campusconnect.academic.repository.EnrollmentRepository;
import ec.edu.udla.campusconnect.academic.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcademicServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private AcademicService academicService;

    @Test
    void createsStudentAsActiveWhenDocumentNumberIsUnique() {
        CreateStudentRequest request = new CreateStudentRequest(
                "STU-001",
                "0102030405",
                "Ana",
                "Lopez",
                LocalDate.of(2012, 5, 10),
                "ana.representante@example.com",
                "0999999999"
        );
        when(studentRepository.existsByDocumentNumber("0102030405")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponse response = academicService.createStudent(request);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(StudentStatus.ACTIVE);
        assertThat(response.studentCode()).isEqualTo("STU-001");
        assertThat(response.fullName()).isEqualTo("Ana Lopez");
        assertThat(response.financialStatus()).isEqualTo("PENDING");
    }

    @Test
    void rejectsDuplicateStudentDocumentNumber() {
        CreateStudentRequest request = new CreateStudentRequest(
                "STU-002",
                "0102030405",
                "Mario",
                "Perez",
                LocalDate.of(2011, 7, 20),
                "mario.representante@example.com",
                "0988888888"
        );
        when(studentRepository.existsByDocumentNumber("0102030405")).thenReturn(true);

        assertThatThrownBy(() -> academicService.createStudent(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("document number");
    }

    @Test
    void createsEnrollmentForExistingStudent() {
        Student student = Student.create(
                "STU-001",
                "0102030405",
                "Ana",
                "Lopez",
                LocalDate.of(2012, 5, 10),
                "ana.representante@example.com",
                "0999999999"
        );
        CreateEnrollmentRequest request = new CreateEnrollmentRequest(
                "STU-001",
                "SCH-001",
                "8vo EGB",
                "2026-2027",
                new BigDecimal("120.00")
        );
        when(studentRepository.findByStudentCode("STU-001")).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByStudentStudentCodeAndAcademicYear("STU-001", "2026-2027")).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EnrollmentResponse response = academicService.createEnrollment(request);

        assertThat(response.studentCode()).isEqualTo("STU-001");
        assertThat(response.status()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(response.pendingAmount()).isEqualByComparingTo("120.00");
    }

    @Test
    void throwsNotFoundWhenStudentDoesNotExist() {
        when(studentRepository.findByStudentCode("STU-404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> academicService.getStudentByCode("STU-404"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("STU-404");
    }
}
