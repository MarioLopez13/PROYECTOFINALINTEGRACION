package ec.edu.udla.campusconnect.academic.service;

import ec.edu.udla.campusconnect.academic.dto.CreateEnrollmentRequest;
import ec.edu.udla.campusconnect.academic.dto.CreateStudentRequest;
import ec.edu.udla.campusconnect.academic.dto.EnrollmentResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentEventResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentResponse;
import ec.edu.udla.campusconnect.academic.dto.UpdateStudentRequest;
import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import ec.edu.udla.campusconnect.academic.entity.Student;
import ec.edu.udla.campusconnect.academic.entity.StudentEvent;
import ec.edu.udla.campusconnect.academic.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.academic.exception.ResourceNotFoundException;
import ec.edu.udla.campusconnect.academic.messaging.BusinessEvent;
import ec.edu.udla.campusconnect.academic.messaging.CampusEventPublisher;
import ec.edu.udla.campusconnect.academic.repository.EnrollmentRepository;
import ec.edu.udla.campusconnect.academic.repository.StudentEventRepository;
import ec.edu.udla.campusconnect.academic.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AcademicService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentEventRepository studentEventRepository;
    private final CampusEventPublisher eventPublisher;

    public AcademicService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository,
                           StudentEventRepository studentEventRepository, CampusEventPublisher eventPublisher) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentEventRepository = studentEventRepository;
        this.eventPublisher = eventPublisher;
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
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        BusinessEvent event = eventPublisher.publishStudentEnrolled(savedEnrollment);
        studentEventRepository.save(StudentEvent.from(
                event,
                student.getStudentCode(),
                "Enrollment created for " + savedEnrollment.getAcademicYear() + " in " + savedEnrollment.getGrade()
        ));
        return EnrollmentResponse.from(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(String studentCode) {
        findStudent(studentCode);
        return enrollmentRepository.findByStudentStudentCode(studentCode).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentEventResponse> getStudentEvents(String studentCode) {
        findStudent(studentCode);
        return studentEventRepository.findByStudentCodeOrderByOccurredAtDesc(studentCode).stream()
                .map(StudentEventResponse::from)
                .toList();
    }

    @Transactional
    public void updateFinancialStatusFromPayment(BusinessEvent event) {
        if (studentEventRepository.existsBySourceEventId(event.eventId())) {
            return;
        }

        String studentCode = dataValue(event, "studentCode", event.entityId());
        Student student = findStudent(studentCode);
        String previousStatus = student.getFinancialStatus();
        student.markFinancialStatus("PAID");

        studentEventRepository.save(StudentEvent.from(
                event,
                studentCode,
                "Payment confirmed: " + dataValue(event, "paymentCode", "without-code")
        ));

        BusinessEvent statusEvent = eventPublisher.publishStudentStatusUpdated(
                student,
                previousStatus,
                "PAID",
                event.correlationId()
        );
        studentEventRepository.save(StudentEvent.from(
                statusEvent,
                studentCode,
                "Financial status updated from " + previousStatus + " to PAID"
        ));
    }

    private Student findStudent(String studentCode) {
        return studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentCode));
    }

    private String dataValue(BusinessEvent event, String key, String fallback) {
        Map<String, Object> data = event.data();
        if (data == null || data.get(key) == null) {
            return fallback;
        }
        return String.valueOf(data.get(key));
    }
}
