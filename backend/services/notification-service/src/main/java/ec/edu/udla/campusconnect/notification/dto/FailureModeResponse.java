package ec.edu.udla.campusconnect.notification.dto;

public record FailureModeResponse(
        boolean nextNotificationWillFail,
        String message
) {
}
