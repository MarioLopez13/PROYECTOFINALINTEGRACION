package ec.edu.udla.campusconnect.academic.messaging;

import ec.edu.udla.campusconnect.academic.service.AcademicService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConfirmedConsumer {

    private final AcademicService academicService;

    public PaymentConfirmedConsumer(AcademicService academicService) {
        this.academicService = academicService;
    }

    @RabbitListener(queues = CampusRabbitConfig.ACADEMIC_PAYMENT_QUEUE)
    public void onPaymentConfirmed(BusinessEvent event) {
        if ("PaymentConfirmed".equals(event.eventType())) {
            academicService.updateFinancialStatusFromPayment(event);
        }
    }
}
