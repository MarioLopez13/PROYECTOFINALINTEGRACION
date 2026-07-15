package ec.edu.udla.campusconnect.analytics.entity;

import ec.edu.udla.campusconnect.analytics.messaging.BusinessEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "analytics_snapshots")
public class AnalyticsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long enrolledStudents;

    @Column(nullable = false)
    private long paymentsConfirmed;

    @Column(nullable = false)
    private long paymentsPending;

    @Column(nullable = false)
    private long attendanceRecords;

    @Column(nullable = false)
    private long incidentsReported;

    @Column(nullable = false)
    private long notificationsSent;

    @Column(nullable = false)
    private long failedMessages;

    @Column(nullable = false)
    private long eventsProcessed;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    public static AnalyticsSnapshot empty() {
        return new AnalyticsSnapshot();
    }

    public void apply(BusinessEvent event) {
        eventsProcessed++;
        switch (event.eventType()) {
            case "StudentEnrolled" -> enrolledStudents++;
            case "PaymentCreated" -> paymentsPending++;
            case "PaymentConfirmed" -> {
                paymentsConfirmed++;
                if (paymentsPending > 0) {
                    paymentsPending--;
                }
            }
            case "AttendanceRecorded" -> attendanceRecords++;
            case "IncidentReported" -> incidentsReported++;
            case "NotificationSent" -> notificationsSent++;
            case "NotificationFailed" -> failedMessages++;
            default -> {
            }
        }
    }

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public long getEnrolledStudents() {
        return enrolledStudents;
    }

    public long getPaymentsConfirmed() {
        return paymentsConfirmed;
    }

    public long getPaymentsPending() {
        return paymentsPending;
    }

    public long getAttendanceRecords() {
        return attendanceRecords;
    }

    public long getIncidentsReported() {
        return incidentsReported;
    }

    public long getNotificationsSent() {
        return notificationsSent;
    }

    public long getFailedMessages() {
        return failedMessages;
    }

    public long getEventsProcessed() {
        return eventsProcessed;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
