package ec.edu.udla.campusconnect.academic.messaging;

import ec.edu.udla.campusconnect.academic.entity.Enrollment;
import ec.edu.udla.campusconnect.academic.entity.Student;
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

    public BusinessEvent publishStudentEnrolled(Enrollment enrollment) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studentCode", enrollment.getStudent().getStudentCode());
        data.put("studentName", enrollment.getStudent().getFullName());
        data.put("representativeEmail", enrollment.getStudent().getRepresentativeEmail());
        data.put("schoolId", enrollment.getSchoolId());
        data.put("grade", enrollment.getGrade());
        data.put("academicYear", enrollment.getAcademicYear());
        data.put("pendingAmount", enrollment.getPendingAmount());

        BusinessEvent event = build("StudentEnrolled", enrollment.getStudent().getStudentCode(), data, null);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "student.enrolled", event);
        return event;
    }

    public BusinessEvent publishStudentStatusUpdated(Student student, String previousStatus, String newStatus,
                                                     String correlationId) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studentCode", student.getStudentCode());
        data.put("studentName", student.getFullName());
        data.put("previousFinancialStatus", previousStatus);
        data.put("newFinancialStatus", newStatus);

        BusinessEvent event = build("StudentStatusUpdated", student.getStudentCode(), data, correlationId);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "student.status-updated", event);
        return event;
    }

    private BusinessEvent build(String eventType, String entityId, Map<String, Object> data, String correlationId) {
        String resolvedCorrelationId = correlationId == null || correlationId.isBlank()
                ? "corr-" + UUID.randomUUID()
                : correlationId;
        return new BusinessEvent(
                "evt-" + UUID.randomUUID(),
                eventType,
                OffsetDateTime.now(),
                resolvedCorrelationId,
                entityId,
                data
        );
    }
}
