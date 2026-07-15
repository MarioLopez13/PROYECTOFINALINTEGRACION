package ec.edu.udla.campusconnect.attendance.messaging;

import ec.edu.udla.campusconnect.attendance.entity.AttendanceRecord;
import ec.edu.udla.campusconnect.attendance.entity.IncidentReport;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CampusEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CampusEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public BusinessEvent publishAttendanceRecorded(AttendanceRecord record) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("attendanceId", record.getId());
        data.put("studentCode", record.getStudentCode());
        data.put("classDate", record.getClassDate());
        data.put("type", record.getType().name());
        data.put("recordedBy", record.getRecordedBy());
        data.put("notes", record.getNotes());

        BusinessEvent event = build("AttendanceRecorded", record.getStudentCode(), data);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "attendance.recorded", event);
        return event;
    }

    public BusinessEvent publishIncidentReported(IncidentReport incident) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("incidentId", incident.getId());
        data.put("studentCode", incident.getStudentCode());
        data.put("severity", incident.getSeverity().name());
        data.put("title", incident.getTitle());
        data.put("description", incident.getDescription());
        data.put("reportedBy", incident.getReportedBy());
        data.put("reportedAt", incident.getReportedAt());

        BusinessEvent event = build("IncidentReported", incident.getStudentCode(), data);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "incident.reported", event);
        return event;
    }

    private BusinessEvent build(String eventType, String entityId, Map<String, Object> data) {
        return new BusinessEvent(
                "evt-" + UUID.randomUUID(),
                eventType,
                OffsetDateTime.now(),
                "corr-" + UUID.randomUUID(),
                entityId,
                data
        );
    }
}
