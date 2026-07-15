package ec.edu.udla.campusconnect.notification.controller;

import ec.edu.udla.campusconnect.notification.dto.FailureModeResponse;
import ec.edu.udla.campusconnect.notification.dto.NotificationResponse;
import ec.edu.udla.campusconnect.notification.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestParam(required = false) String studentCode) {
        return notificationService.getNotifications(studentCode);
    }

    @PostMapping("/simulate-failure")
    public FailureModeResponse failNextNotification() {
        return notificationService.failNextNotification();
    }

    @GetMapping("/simulate-failure")
    public FailureModeResponse getFailureMode() {
        return notificationService.getFailureMode();
    }
}
