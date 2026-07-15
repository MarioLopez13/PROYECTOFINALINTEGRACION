package ec.edu.udla.campusconnect.analytics;

import ec.edu.udla.campusconnect.analytics.repository.AnalyticsEventRepository;
import ec.edu.udla.campusconnect.analytics.repository.AnalyticsSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
        "spring.rabbitmq.listener.simple.auto-startup=false"
})
class AnalyticsApplicationTests {

    @MockBean
    private AnalyticsSnapshotRepository analyticsSnapshotRepository;

    @MockBean
    private AnalyticsEventRepository analyticsEventRepository;

    @Test
    void contextLoads() {
    }
}
