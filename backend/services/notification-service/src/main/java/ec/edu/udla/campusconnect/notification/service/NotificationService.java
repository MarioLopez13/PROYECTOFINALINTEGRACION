package ec.edu.udla.campusconnect.notification.service;

import ec.edu.udla.campusconnect.notification.dto.FailureModeResponse;
import ec.edu.udla.campusconnect.notification.dto.NotificationResponse;
import ec.edu.udla.campusconnect.notification.entity.Notification;
import ec.edu.udla.campusconnect.notification.messaging.BusinessEvent;
import ec.edu.udla.campusconnect.notification.messaging.CampusEventPublisher;
import ec.edu.udla.campusconnect.notification.repository.NotificationRepository;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CampusEventPublisher eventPublisher;
    private final AtomicBoolean failNextNotification = new AtomicBoolean(false);

    public NotificationService(NotificationRepository notificationRepository, CampusEventPublisher eventPublisher) {
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
    }

    public void processBusinessEvent(BusinessEvent event) {
        if (notificationRepository.existsBySourceEventId(event.eventId())) {
            return;
        }

        NotificationDraft draft = translate(event);
        if (failNextNotification.getAndSet(false)) {
            String reason = "Controlled failure requested from demo endpoint";
            Notification failed = notificationRepository.save(Notification.failed(
                    event.eventId(),
                    event.eventType(),
                    draft.studentCode(),
                    draft.recipient(),
                    draft.subject(),
                    draft.message(),
                    reason,
                    event.correlationId()
            ));
            eventPublisher.publishNotificationFailed(failed, reason);
            throw new AmqpRejectAndDontRequeueException(reason);
        }

        Notification sent = notificationRepository.save(Notification.sent(
                event.eventId(),
                event.eventType(),
                draft.studentCode(),
                draft.recipient(),
                draft.subject(),
                draft.message(),
                event.correlationId()
        ));
        eventPublisher.publishNotificationSent(sent);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String studentCode) {
        List<Notification> notifications = studentCode == null || studentCode.isBlank()
                ? notificationRepository.findAllByOrderByCreatedAtDesc()
                : notificationRepository.findByStudentCodeOrderByCreatedAtDesc(studentCode);
        return notifications.stream().map(NotificationResponse::from).toList();
    }

    public FailureModeResponse failNextNotification() {
        failNextNotification.set(true);
        return new FailureModeResponse(true, "The next consumed business event will be sent to the DLQ");
    }

    public FailureModeResponse getFailureMode() {
        return new FailureModeResponse(
                failNextNotification.get(),
                failNextNotification.get()
                        ? "The next consumed business event will fail"
                        : "Notification processing is healthy"
        );
    }

    private NotificationDraft translate(BusinessEvent event) {
        String studentCode = dataValue(event, "studentCode", event.entityId());
        String recipient = dataValue(event, "representativeEmail", studentCode.toLowerCase() + "@representantes.demo");
        return switch (event.eventType()) {
            case "StudentEnrolled" -> new NotificationDraft(
                    studentCode,
                    recipient,
                    "Matricula registrada",
                    "El estudiante " + dataValue(event, "studentName", studentCode)
                            + " fue matriculado en " + dataValue(event, "grade", "grado asignado") + "."
            );
            case "PaymentConfirmed" -> new NotificationDraft(
                    studentCode,
                    recipient,
                    "Pago confirmado",
                    "Se confirmo el pago " + dataValue(event, "paymentCode", event.entityId())
                            + " por " + dataValue(event, "amount", "0.00") + "."
            );
            case "AttendanceRecorded" -> new NotificationDraft(
                    studentCode,
                    recipient,
                    "Asistencia registrada",
                    "Se registro asistencia tipo " + dataValue(event, "type", "PRESENT")
                            + " para el estudiante " + studentCode + "."
            );
            case "IncidentReported" -> new NotificationDraft(
                    studentCode,
                    recipient,
                    "Alerta de bienestar",
                    "Se reporto una novedad " + dataValue(event, "severity", "MEDIUM")
                            + ": " + dataValue(event, "title", "Incidente registrado") + "."
            );
            default -> new NotificationDraft(
                    studentCode,
                    recipient,
                    "Evento procesado",
                    "Se proceso el evento " + event.eventType() + "."
            );
        };
    }

    private String dataValue(BusinessEvent event, String key, String fallback) {
        Map<String, Object> data = event.data();
        if (data == null || data.get(key) == null) {
            return fallback;
        }
        return String.valueOf(data.get(key));
    }

    private record NotificationDraft(
            String studentCode,
            String recipient,
            String subject,
            String message
    ) {
    }
}
