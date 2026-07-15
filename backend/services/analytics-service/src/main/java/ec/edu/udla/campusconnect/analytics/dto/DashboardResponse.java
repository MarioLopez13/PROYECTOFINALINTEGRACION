package ec.edu.udla.campusconnect.analytics.dto;

import ec.edu.udla.campusconnect.analytics.entity.AnalyticsSnapshot;

import java.time.OffsetDateTime;

public record DashboardResponse(
        long enrolledStudents,
        long paymentsConfirmed,
        long paymentsPending,
        long attendanceRecords,
        long incidentsReported,
        long eventsProcessed,
        long failedMessages,
        long notificationsSent,
        String ecosystemStatus,
        OffsetDateTime updatedAt
) {
    public static DashboardResponse from(AnalyticsSnapshot snapshot) {
        return new DashboardResponse(
                snapshot.getEnrolledStudents(),
                snapshot.getPaymentsConfirmed(),
                snapshot.getPaymentsPending(),
                snapshot.getAttendanceRecords(),
                snapshot.getIncidentsReported(),
                snapshot.getEventsProcessed(),
                snapshot.getFailedMessages(),
                snapshot.getNotificationsSent(),
                snapshot.getFailedMessages() > 0 ? "DEGRADED" : "HEALTHY",
                snapshot.getUpdatedAt()
        );
    }
}
