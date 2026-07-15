package ec.edu.udla.campusconnect.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank String paymentCode,
        @NotBlank String studentCode,
        @NotBlank String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount
) {
}
