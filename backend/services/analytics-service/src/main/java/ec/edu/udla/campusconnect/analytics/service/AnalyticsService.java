package ec.edu.udla.campusconnect.analytics.service;

import ec.edu.udla.campusconnect.analytics.dto.AnalyticsEventResponse;
import ec.edu.udla.campusconnect.analytics.dto.DashboardResponse;
import ec.edu.udla.campusconnect.analytics.entity.AnalyticsEvent;
import ec.edu.udla.campusconnect.analytics.entity.AnalyticsSnapshot;
import ec.edu.udla.campusconnect.analytics.messaging.BusinessEvent;
import ec.edu.udla.campusconnect.analytics.repository.AnalyticsEventRepository;
import ec.edu.udla.campusconnect.analytics.repository.AnalyticsSnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final AnalyticsSnapshotRepository snapshotRepository;
    private final AnalyticsEventRepository eventRepository;

    public AnalyticsService(AnalyticsSnapshotRepository snapshotRepository, AnalyticsEventRepository eventRepository) {
        this.snapshotRepository = snapshotRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void process(BusinessEvent event) {
        if (eventRepository.existsBySourceEventId(event.eventId())) {
            return;
        }

        AnalyticsSnapshot snapshot = getOrCreateSnapshot();
        snapshot.apply(event);
        eventRepository.save(AnalyticsEvent.from(event, summarize(event)));
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        return DashboardResponse.from(getExistingSnapshot());
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventResponse> getRecentEvents() {
        return eventRepository.findTop50ByOrderByProcessedAtDesc().stream()
                .map(AnalyticsEventResponse::from)
                .toList();
    }

    private AnalyticsSnapshot getOrCreateSnapshot() {
        return snapshotRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> snapshotRepository.save(AnalyticsSnapshot.empty()));
    }

    private AnalyticsSnapshot getExistingSnapshot() {
        return snapshotRepository.findAll().stream()
                .findFirst()
                .orElseGet(AnalyticsSnapshot::empty);
    }

    private String summarize(BusinessEvent event) {
        Map<String, Object> data = event.data();
        String studentCode = data != null && data.get("studentCode") != null
                ? String.valueOf(data.get("studentCode"))
                : event.entityId();
        return switch (event.eventType()) {
            case "StudentEnrolled" -> "Student enrolled: " + studentCode;
            case "PaymentCreated" -> "Pending payment created: " + event.entityId();
            case "PaymentConfirmed" -> "Payment confirmed for " + studentCode;
            case "AttendanceRecorded" -> "Attendance recorded for " + studentCode;
            case "IncidentReported" -> "Incident reported for " + studentCode;
            case "NotificationSent" -> "Notification sent for " + studentCode;
            case "NotificationFailed" -> "Notification failed for " + studentCode;
            case "StudentStatusUpdated" -> "Student status updated: " + studentCode;
            default -> "Event processed: " + event.eventType();
        };
    }
}
