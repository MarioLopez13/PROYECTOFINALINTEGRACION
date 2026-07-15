package ec.edu.udla.campusconnect.payment.messaging;

import ec.edu.udla.campusconnect.payment.entity.Payment;
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

    public BusinessEvent publishPaymentCreated(Payment payment) {
        Map<String, Object> data = paymentData(payment);
        BusinessEvent event = build("PaymentCreated", payment.getPaymentCode(), data, null);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "payment.created", event);
        return event;
    }

    public BusinessEvent publishPaymentConfirmed(Payment payment) {
        Map<String, Object> data = paymentData(payment);
        data.put("confirmationReference", payment.getConfirmationReference());
        data.put("confirmedAt", payment.getConfirmedAt());
        BusinessEvent event = build("PaymentConfirmed", payment.getPaymentCode(), data, null);
        rabbitTemplate.convertAndSend(CampusRabbitConfig.EVENTS_EXCHANGE, "payment.confirmed", event);
        return event;
    }

    private Map<String, Object> paymentData(Payment payment) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("paymentCode", payment.getPaymentCode());
        data.put("studentCode", payment.getStudentCode());
        data.put("description", payment.getDescription());
        data.put("amount", payment.getAmount());
        data.put("status", payment.getStatus().name());
        return data;
    }

    private BusinessEvent build(String eventType, String entityId, Map<String, Object> data, String correlationId) {
        String resolvedCorrelationId = correlationId == null || correlationId.isBlank()
                ? "corr-" + UUID.randomUUID()
                : correlationId;
        return new BusinessEvent(
                "evt-" + UUID.randomUUID(),
                eventType,
                OffsetDateTime.now(),
                resolvedCorrelationId,
                entityId,
                data
        );
    }
}
