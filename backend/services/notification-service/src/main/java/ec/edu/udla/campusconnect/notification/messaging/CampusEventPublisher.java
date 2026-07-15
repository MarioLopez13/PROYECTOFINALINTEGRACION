package ec.edu.udla.campusconnect.notification.messaging;

import ec.edu.udla.campusconnect.notification.entity.Notification;
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

    public BusinessEvent publishNotificationSent(Notification notification) {
        BusinessEvent event = build("NotificationSent", notification, null);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "notification.sent", event);
        return event;
    }

    public BusinessEvent publishNotificationFailed(Notification notification, String failureReason) {
        BusinessEvent event = build("NotificationFailed", notification, failureReason);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "notification.failed", event);
        return event;
    }

    private BusinessEvent build(String eventType, Notification notification, String failureReason) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("notificationId", notification.getId());
        data.put("sourceEventId", notification.getSourceEventId());
        data.put("sourceEventType", notification.getEventType());
        data.put("studentCode", notification.getStudentCode());
        data.put("recipient", notification.getRecipient());
        data.put("status", notification.getStatus().name());
        data.put("failureReason", failureReason);

        return new BusinessEvent(
                "evt-" + UUID.randomUUID(),
                eventType,
                OffsetDateTime.now(),
                notification.getCorrelationId(),
                String.valueOf(notification.getId()),
                data
        );
    }
}
