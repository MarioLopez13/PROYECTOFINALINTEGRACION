package ec.edu.udla.campusconnect.analytics.repository;

import ec.edu.udla.campusconnect.analytics.entity.AnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsSnapshotRepository extends JpaRepository<AnalyticsSnapshot, Long> {
}
