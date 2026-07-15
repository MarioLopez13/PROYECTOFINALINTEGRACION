package ec.edu.udla.campusconnect.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ConfirmPaymentRequest(
        @NotBlank String confirmationReference,
        @NotNull OffsetDateTime confirmedAt
) {
}
