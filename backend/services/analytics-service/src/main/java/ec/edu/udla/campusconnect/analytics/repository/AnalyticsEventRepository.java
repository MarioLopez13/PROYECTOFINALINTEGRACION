package ec.edu.udla.campusconnect.analytics.repository;

import ec.edu.udla.campusconnect.analytics.entity.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
    boolean existsBySourceEventId(String sourceEventId);

    List<AnalyticsEvent> findTop50ByOrderByProcessedAtDesc();
}
