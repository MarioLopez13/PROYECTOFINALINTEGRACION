package ec.edu.udla.campusconnect.attendance.messaging;

import java.time.OffsetDateTime;
import java.util.Map;

public record BusinessEvent(
        String eventId,
        String eventType,
        OffsetDateTime occurredAt,
        String correlationId,
        String entityId,
        Map<String, Object> data
) {
}
