package ec.edu.udla.campusconnect.payment.dto;

import ec.edu.udla.campusconnect.payment.entity.Payment;
import ec.edu.udla.campusconnect.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        Long id,
        String paymentCode,
        String studentCode,
        String description,
        BigDecimal amount,
        PaymentStatus status,
        String confirmationReference,
        OffsetDateTime confirmedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentCode(),
                payment.getStudentCode(),
                payment.getDescription(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getConfirmationReference(),
                payment.getConfirmedAt(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
