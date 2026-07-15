package ec.edu.udla.campusconnect.analytics.controller;

import ec.edu.udla.campusconnect.analytics.dto.AnalyticsEventResponse;
import ec.edu.udla.campusconnect.analytics.dto.DashboardResponse;
import ec.edu.udla.campusconnect.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return analyticsService.getDashboard();
    }

    @GetMapping("/events")
    public List<AnalyticsEventResponse> getRecentEvents() {
        return analyticsService.getRecentEvents();
    }
}
