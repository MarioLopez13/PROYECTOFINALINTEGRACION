package ec.edu.udla.campusconnect.notification.repository;

import ec.edu.udla.campusconnect.notification.entity.Notification;
import ec.edu.udla.campusconnect.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    boolean existsBySourceEventId(String sourceEventId);

    long countByStatus(NotificationStatus status);

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findByStudentCodeOrderByCreatedAtDesc(String studentCode);
}
