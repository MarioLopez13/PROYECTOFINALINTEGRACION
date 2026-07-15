package ec.edu.udla.campusconnect.academic.entity;

import ec.edu.udla.campusconnect.academic.messaging.BusinessEvent;
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
        name = "student_events",
        indexes = {
                @Index(name = "idx_student_events_student", columnList = "studentCode"),
                @Index(name = "idx_student_events_correlation", columnList = "correlationId")
        },
        uniqueConstraints = @UniqueConstraint(name = "uk_student_event_source", columnNames = "sourceEventId")
)
public class StudentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sourceEventId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, length = 30)
    private String studentCode;

    @Column(nullable = false, length = 80)
    private String correlationId;

    @Column(nullable = false, length = 500)
    private String details;

    @Column(nullable = false)
    private OffsetDateTime occurredAt;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    protected StudentEvent() {
    }

    private StudentEvent(String sourceEventId, String eventType, String studentCode, String correlationId,
                         String details, OffsetDateTime occurredAt) {
        this.sourceEventId = sourceEventId;
        this.eventType = eventType;
        this.studentCode = studentCode;
        this.correlationId = correlationId;
        this.details = details;
        this.occurredAt = occurredAt;
    }

    public static StudentEvent from(BusinessEvent event, String studentCode, String details) {
        return new StudentEvent(
                event.eventId(),
                event.eventType(),
                studentCode,
                event.correlationId(),
                details,
                event.occurredAt()
        );
    }

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
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

    public String getStudentCode() {
        return studentCode;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getDetails() {
        return details;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
