package ec.edu.udla.campusconnect.payment.service;

import ec.edu.udla.campusconnect.payment.dto.ConfirmPaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.CreatePaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.PaymentResponse;
import ec.edu.udla.campusconnect.payment.entity.Payment;
import ec.edu.udla.campusconnect.payment.entity.PaymentStatus;
import ec.edu.udla.campusconnect.payment.exception.BusinessRuleException;
import ec.edu.udla.campusconnect.payment.exception.ResourceNotFoundException;
import ec.edu.udla.campusconnect.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        if (paymentRepository.existsByPaymentCode(request.paymentCode())) {
            throw new BusinessRuleException("Payment code already exists");
        }
        Payment payment = Payment.create(
                request.paymentCode(),
                request.studentCode(),
                request.description(),
                request.amount()
        );
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse confirmPayment(String paymentCode, ConfirmPaymentRequest request) {
        Payment payment = findPayment(paymentCode);
        payment.confirm(request.confirmationReference(), request.confirmedAt());
        return PaymentResponse.from(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStudent(String studentCode) {
        return paymentRepository.findByStudentCode(studentCode).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentCode) {
        return PaymentResponse.from(findPayment(paymentCode));
    }

    private Payment findPayment(String paymentCode) {
        return paymentRepository.findByPaymentCode(paymentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentCode));
    }
}
