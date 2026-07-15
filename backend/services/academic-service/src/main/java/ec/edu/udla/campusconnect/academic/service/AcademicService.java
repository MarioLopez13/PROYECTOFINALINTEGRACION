package ec.edu.udla.campusconnect.academic.service;

import ec.edu.udla.campusconnect.academic.dto.CreateEnrollmentRequest;
import ec.edu.udla.campusconnect.academic.dto.CreateStudentRequest;
import ec.edu.udla.campusconnect.academic.dto.EnrollmentResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentResponse;
import ec.edu.udla.campusconnect.academic.dto.UpdateStudentRequest;
import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import ec.edu.udla.campusconnect.academic.entity.Student;
import ec.edu.udla.campusconnect.academic.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.academic.exception.ResourceNotFoundException;
import ec.edu.udla.campusconnect.academic.repository.EnrollmentRepository;
import ec.edu.udla.campusconnect.academic.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcademicService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AcademicService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (studentRepository.existsByStudentCode(request.studentCode())) {
            throw new BusinessRuleException("Student code already exists");
        }
        if (studentRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new BusinessRuleException("Student document number already exists");
        }
        Student student = Student.create(
                request.studentCode(),
                request.documentNumber(),
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.representativeEmail(),
                request.representativePhone()
        );
        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudents() {
        return studentRepository.findAll().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByCode(String studentCode) {
        return StudentResponse.from(findStudent(studentCode));
    }

    @Transactional
    public StudentResponse updateStudent(String studentCode, UpdateStudentRequest request) {
        Student student = findStudent(studentCode);
        student.updateProfile(
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.representativeEmail(),
                request.representativePhone()
        );
        return StudentResponse.from(student);
    }

    @Transactional
    public void deactivateStudent(String studentCode) {
        Student student = findStudent(studentCode);
        student.markInactive();
    }

    @Transactional
    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request) {
        Student student = findStudent(request.studentCode());
        if (enrollmentRepository.existsByStudentStudentCodeAndAcademicYear(request.studentCode(), request.academicYear())) {
            throw new BusinessRuleException("Student is already enrolled in academic year " + request.academicYear());
        }
        Enrollment enrollment = Enrollment.create(
                student,
                request.schoolId(),
                request.grade(),
                request.academicYear(),
                request.pendingAmount()
        );
        return EnrollmentResponse.from(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(String studentCode) {
        findStudent(studentCode);
        return enrollmentRepository.findByStudentStudentCode(studentCode).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }

    private Student findStudent(String studentCode) {
        return studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentCode));
    }
}
