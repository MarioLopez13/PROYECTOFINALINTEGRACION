package ec.edu.udla.campusconnect.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
        name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_student", columnList = "studentCode"),
                @Index(name = "idx_notifications_status", columnList = "status")
        },
        uniqueConstraints = @UniqueConstraint(name = "uk_notification_source_event", columnNames = "sourceEventId")
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sourceEventId;

    @Column(nullable = false, length = 50)
    private String eventType;

    @Column(nullable = false, length = 30)
    private String studentCode;

    @Column(nullable = false, length = 120)
    private String recipient;

    @Column(nullable = false, length = 160)
    private String subject;

    @Column(nullable = false, length = 600)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    @Column(length = 300)
    private String failureReason;

    @Column(nullable = false, length = 80)
    private String correlationId;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    protected Notification() {
    }

    private Notification(String sourceEventId, String eventType, String studentCode, String recipient,
                         String subject, String message, NotificationStatus status, String failureReason,
                         String correlationId) {
        this.sourceEventId = sourceEventId;
        this.eventType = eventType;
        this.studentCode = studentCode;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.failureReason = failureReason;
        this.correlationId = correlationId;
    }

    public static Notification sent(String sourceEventId, String eventType, String studentCode, String recipient,
                                    String subject, String message, String correlationId) {
        return new Notification(sourceEventId, eventType, studentCode, recipient, subject, message,
                NotificationStatus.SENT, null, correlationId);
    }

    public static Notification failed(String sourceEventId, String eventType, String studentCode, String recipient,
                                      String subject, String message, String failureReason, String correlationId) {
        return new Notification(sourceEventId, eventType, studentCode, recipient, subject, message,
                NotificationStatus.FAILED, failureReason, correlationId);
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

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
