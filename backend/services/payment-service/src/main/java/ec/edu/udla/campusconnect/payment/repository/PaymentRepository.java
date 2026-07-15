package ec.edu.udla.campusconnect.payment.repository;

import ec.edu.udla.campusconnect.payment.entity.Payment;
import ec.edu.udla.campusconnect.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPaymentCode(String paymentCode);

    Optional<Payment> findByPaymentCode(String paymentCode);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByStudentCode(String studentCode);
}
