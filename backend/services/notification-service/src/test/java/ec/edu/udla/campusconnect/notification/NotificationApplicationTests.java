package ec.edu.udla.campusconnect.notification;

import ec.edu.udla.campusconnect.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
        "spring.rabbitmq.listener.simple.auto-startup=false"
})
class NotificationApplicationTests {

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    void contextLoads() {
    }
}
