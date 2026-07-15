package ec.edu.udla.campusconnect.attendance.controller;

import ec.edu.udla.campusconnect.attendance.dto.AttendanceResponse;
import ec.edu.udla.campusconnect.attendance.dto.CreateAttendanceRequest;
import ec.edu.udla.campusconnect.attendance.dto.CreateIncidentRequest;
import ec.edu.udla.campusconnect.attendance.dto.IncidentResponse;
import ec.edu.udla.campusconnect.attendance.dto.StudentWellbeingHistoryResponse;
import ec.edu.udla.campusconnect.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/records")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceResponse recordAttendance(@Valid @RequestBody CreateAttendanceRequest request) {
        return attendanceService.recordAttendance(request);
    }

    @GetMapping("/records")
    public List<AttendanceResponse> getAttendanceRecords(@RequestParam(required = false) String studentCode) {
        return attendanceService.getAttendanceRecords(studentCode);
    }

    @PostMapping("/incidents")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse reportIncident(@Valid @RequestBody CreateIncidentRequest request) {
        return attendanceService.reportIncident(request);
    }

    @GetMapping("/incidents")
    public List<IncidentResponse> getIncidents(@RequestParam(required = false) String studentCode) {
        return attendanceService.getIncidents(studentCode);
    }

    @GetMapping("/students/{studentCode}/history")
    public StudentWellbeingHistoryResponse getStudentHistory(@PathVariable String studentCode) {
        return attendanceService.getStudentHistory(studentCode);
    }
}
