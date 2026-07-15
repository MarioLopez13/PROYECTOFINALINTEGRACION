package ec.edu.udla.campusconnect.notification.dto;

import ec.edu.udla.campusconnect.notification.entity.Notification;
import ec.edu.udla.campusconnect.notification.entity.NotificationStatus;

import java.time.OffsetDateTime;

public record NotificationResponse(
        Long id,
        String sourceEventId,
        String eventType,
        String studentCode,
        String recipient,
        String subject,
        String message,
        NotificationStatus status,
        String failureReason,
        String correlationId,
        OffsetDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getSourceEventId(),
                notification.getEventType(),
                notification.getStudentCode(),
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getFailureReason(),
                notification.getCorrelationId(),
                notification.getCreatedAt()
        );
    }
}
