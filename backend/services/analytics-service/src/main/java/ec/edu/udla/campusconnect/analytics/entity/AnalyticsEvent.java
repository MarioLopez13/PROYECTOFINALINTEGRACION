package ec.edu.udla.campusconnect.analytics.entity;

import ec.edu.udla.campusconnect.analytics.messaging.BusinessEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "analytics_events",
        indexes = {
                @Index(name = "idx_analytics_events_type", columnList = "eventType"),
                @Index(name = "idx_analytics_events_correlation", columnList = "correlationId")
        },
        uniqueConstraints = @UniqueConstraint(name = "uk_analytics_event_source", columnNames = "sourceEventId")
)
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sourceEventId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, length = 80)
    private String entityId;

    @Column(nullable = false, length = 80)
    private String correlationId;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(nullable = false)
    private OffsetDateTime occurredAt;

    @Column(nullable = false)
    private OffsetDateTime processedAt;

    protected AnalyticsEvent() {
    }

    private AnalyticsEvent(String sourceEventId, String eventType, String entityId, String correlationId,
                           String summary, OffsetDateTime occurredAt) {
        this.sourceEventId = sourceEventId;
        this.eventType = eventType;
        this.entityId = entityId;
        this.correlationId = correlationId;
        this.summary = summary;
        this.occurredAt = occurredAt;
    }

    public static AnalyticsEvent from(BusinessEvent event, String summary) {
        return new AnalyticsEvent(
                event.eventId(),
                event.eventType(),
                event.entityId(),
                event.correlationId(),
                summary,
                event.occurredAt()
        );
    }

    @PrePersist
    void prePersist() {
        processedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getSourceEventId() {
        return sourceEventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getSummary() {
        return summary;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }
}
