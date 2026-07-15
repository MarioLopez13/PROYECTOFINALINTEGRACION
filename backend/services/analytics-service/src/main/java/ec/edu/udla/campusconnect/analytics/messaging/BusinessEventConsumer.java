package ec.edu.udla.campusconnect.analytics.messaging;

import ec.edu.udla.campusconnect.analytics.service.AnalyticsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BusinessEventConsumer {

    private final AnalyticsService analyticsService;

    public BusinessEventConsumer(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = CampusRabbitConfig.ANALYTICS_EVENTS_QUEUE)
    public void onBusinessEvent(BusinessEvent event) {
        analyticsService.process(event);
    }
}
