package ec.edu.udla.campusconnect.academic.controller;

import ec.edu.udla.campusconnect.academic.dto.CreateEnrollmentRequest;
import ec.edu.udla.campusconnect.academic.dto.CreateStudentRequest;
import ec.edu.udla.campusconnect.academic.dto.EnrollmentResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentEventResponse;
import ec.edu.udla.campusconnect.academic.dto.StudentResponse;
import ec.edu.udla.campusconnect.academic.dto.UpdateStudentRequest;
import ec.edu.udla.campusconnect.academic.service.AcademicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest request) {
        return academicService.createStudent(request);
    }

    @GetMapping("/students")
    public List<StudentResponse> getStudents() {
        return academicService.getStudents();
    }

    @GetMapping("/students/{studentCode}")
    public StudentResponse getStudent(@PathVariable String studentCode) {
        return academicService.getStudentByCode(studentCode);
    }

    @PutMapping("/students/{studentCode}")
    public StudentResponse updateStudent(@PathVariable String studentCode,
                                         @Valid @RequestBody UpdateStudentRequest request) {
        return academicService.updateStudent(studentCode, request);
    }

    @DeleteMapping("/students/{studentCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateStudent(@PathVariable String studentCode) {
        academicService.deactivateStudent(studentCode);
    }

    @PostMapping("/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request) {
        return academicService.createEnrollment(request);
    }

    @GetMapping("/students/{studentCode}/enrollments")
    public List<EnrollmentResponse> getStudentEnrollments(@PathVariable String studentCode) {
        return academicService.getEnrollmentsByStudent(studentCode);
    }

    @GetMapping("/students/{studentCode}/events")
    public List<StudentEventResponse> getStudentEvents(@PathVariable String studentCode) {
        return academicService.getStudentEvents(studentCode);
    }
}
