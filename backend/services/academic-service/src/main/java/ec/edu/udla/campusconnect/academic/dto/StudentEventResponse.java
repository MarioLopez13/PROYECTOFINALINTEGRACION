package ec.edu.udla.campusconnect.academic.dto;

import ec.edu.udla.campusconnect.academic.entity.StudentEvent;

import java.time.OffsetDateTime;

public record StudentEventResponse(
        Long id,
        String sourceEventId,
        String eventType,
        String studentCode,
        String correlationId,
        String details,
        OffsetDateTime occurredAt
) {
    public static StudentEventResponse from(StudentEvent event) {
        return new StudentEventResponse(
                event.getId(),
                event.getSourceEventId(),
                event.getEventType(),
                event.getStudentCode(),
                event.getCorrelationId(),
                event.getDetails(),
                event.getOccurredAt()
        );
    }
}
