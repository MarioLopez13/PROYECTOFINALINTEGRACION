package ec.edu.udla.campusconnect.notification.messaging;

import ec.edu.udla.campusconnect.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BusinessEventConsumer {

    private final NotificationService notificationService;

    public BusinessEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = CampusRabbitConfig.NOTIFICATION_EVENTS_QUEUE)
    public void onBusinessEvent(BusinessEvent event) {
        notificationService.processBusinessEvent(event);
    }
}
