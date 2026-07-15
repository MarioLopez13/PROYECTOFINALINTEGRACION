package ec.edu.udla.campusconnect.payment.entity;

import ec.edu.udla.campusconnect.payment.exception.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String paymentCode;

    @Column(nullable = false, length = 30)
    private String studentCode;

    @Column(nullable = false, length = 160)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(length = 80)
    private String confirmationReference;

    private OffsetDateTime confirmedAt;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    protected Payment() {
    }

    private Payment(String paymentCode, String studentCode, String description, BigDecimal amount) {
        this.paymentCode = paymentCode;
        this.studentCode = studentCode;
        this.description = description;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(String paymentCode, String studentCode, String description, BigDecimal amount) {
        return new Payment(paymentCode, studentCode, description, amount);
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void confirm(String confirmationReference, OffsetDateTime confirmedAt) {
        if (status == PaymentStatus.CONFIRMED) {
            throw new BusinessRuleException("Payment is already confirmed");
        }
        if (status == PaymentStatus.CANCELLED) {
            throw new BusinessRuleException("Cancelled payment cannot be confirmed");
        }
        this.status = PaymentStatus.CONFIRMED;
        this.confirmationReference = confirmationReference;
        this.confirmedAt = confirmedAt;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getConfirmationReference() {
        return confirmationReference;
    }

    public OffsetDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
