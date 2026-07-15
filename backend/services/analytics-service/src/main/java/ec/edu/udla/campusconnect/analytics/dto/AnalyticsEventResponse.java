package ec.edu.udla.campusconnect.analytics.dto;

import ec.edu.udla.campusconnect.analytics.entity.AnalyticsEvent;

import java.time.OffsetDateTime;

public record AnalyticsEventResponse(
        Long id,
        String sourceEventId,
        String eventType,
        String entityId,
        String correlationId,
        String summary,
        OffsetDateTime occurredAt,
        OffsetDateTime processedAt
) {
    public static AnalyticsEventResponse from(AnalyticsEvent event) {
        return new AnalyticsEventResponse(
                event.getId(),
                event.getSourceEventId(),
                event.getEventType(),
                event.getEntityId(),
                event.getCorrelationId(),
                event.getSummary(),
                event.getOccurredAt(),
                event.getProcessedAt()
        );
    }
}
