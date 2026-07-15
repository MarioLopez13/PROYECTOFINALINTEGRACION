package ec.edu.udla.campusconnect.payment.controller;

import ec.edu.udla.campusconnect.payment.dto.ConfirmPaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.CreatePaymentRequest;
import ec.edu.udla.campusconnect.payment.dto.PaymentResponse;
import ec.edu.udla.campusconnect.payment.entity.PaymentStatus;
import ec.edu.udla.campusconnect.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/{paymentCode}/confirm")
    public PaymentResponse confirmPayment(@PathVariable String paymentCode,
                                          @Valid @RequestBody ConfirmPaymentRequest request) {
        return paymentService.confirmPayment(paymentCode, request);
    }

    @GetMapping("/{paymentCode}")
    public PaymentResponse getPayment(@PathVariable String paymentCode) {
        return paymentService.getPayment(paymentCode);
    }

    @GetMapping
    public List<PaymentResponse> getPayments(@RequestParam(required = false) PaymentStatus status,
                                             @RequestParam(required = false) String studentCode) {
        if (studentCode != null && !studentCode.isBlank()) {
            return paymentService.getPaymentsByStudent(studentCode);
        }
        PaymentStatus selectedStatus = status == null ? PaymentStatus.PENDING : status;
        return paymentService.getPaymentsByStatus(selectedStatus);
    }
}
