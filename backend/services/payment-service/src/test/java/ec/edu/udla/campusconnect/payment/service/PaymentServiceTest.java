package ec.edu.udla.campusconnect.payment.service;

import ec.edu.udla.campusconnect.payment.dto.ConfirmPaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.CreatePaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.PaymentResponse;
import ec.edu.udla.campusconnect.payment.entity.Payment;
import ec.edu.udla.campusconnect.payment.entity.PaymentStatus;
import ec.edu.udla.campusconnect.payment.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.payment.exception.ResourceNotFoundException;
import ec.edu.udla.campusconnect.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createsPendingPaymentForStudent() {
        CreatePaymentRequest request = new CreatePaymentRequest(
                "PAY-001",
                "STU-001",
                "Initial enrollment fee",
                new BigDecimal("120.00")
        );
        when(paymentRepository.existsByPaymentCode("PAY-001")).thenReturn(false);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse response = paymentService.createPayment(request);

        assertThat(response.paymentCode()).isEqualTo("PAY-001");
        assertThat(response.studentCode()).isEqualTo("STU-001");
        assertThat(response.status()).isEqualTo(PaymentStatus.PENDING);
        assertThat(response.amount()).isEqualByComparingTo("120.00");
    }

    @Test
    void confirmsPendingPayment() {
        Payment payment = Payment.create("PAY-001", "STU-001", "Initial enrollment fee", new BigDecimal("120.00"));
        ConfirmPaymentRequest request = new ConfirmPaymentRequest("BANK-REF-001", OffsetDateTime.parse("2026-07-15T10:30:00Z"));
        when(paymentRepository.findByPaymentCode("PAY-001")).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.confirmPayment("PAY-001", request);

        assertThat(response.status()).isEqualTo(PaymentStatus.CONFIRMED);
        assertThat(response.confirmationReference()).isEqualTo("BANK-REF-001");
        assertThat(response.confirmedAt()).isEqualTo(OffsetDateTime.parse("2026-07-15T10:30:00Z"));
    }

    @Test
    void rejectsConfirmingAlreadyConfirmedPayment() {
        Payment payment = Payment.create("PAY-001", "STU-001", "Initial enrollment fee", new BigDecimal("120.00"));
        payment.confirm("BANK-REF-001", OffsetDateTime.parse("2026-07-15T10:30:00Z"));
        when(paymentRepository.findByPaymentCode("PAY-001")).thenReturn(Optional.of(payment));

        ConfirmPaymentRequest request = new ConfirmPaymentRequest("BANK-REF-002", OffsetDateTime.parse("2026-07-15T11:00:00Z"));

        assertThatThrownBy(() -> paymentService.confirmPayment("PAY-001", request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("already confirmed");
    }

    @Test
    void listsPaymentsByStatus() {
        Payment pending = Payment.create("PAY-001", "STU-001", "Initial enrollment fee", new BigDecimal("120.00"));
        when(paymentRepository.findByStatus(PaymentStatus.PENDING)).thenReturn(List.of(pending));

        List<PaymentResponse> response = paymentService.getPaymentsByStatus(PaymentStatus.PENDING);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).paymentCode()).isEqualTo("PAY-001");
        assertThat(response.get(0).status()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void throwsNotFoundWhenPaymentDoesNotExist() {
        when(paymentRepository.findByPaymentCode("PAY-404")).thenReturn(Optional.empty());

        ConfirmPaymentRequest request = new ConfirmPaymentRequest("BANK-REF-404", OffsetDateTime.parse("2026-07-15T10:30:00Z"));

        assertThatThrownBy(() -> paymentService.confirmPayment("PAY-404", request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("PAY-404");
    }
}
