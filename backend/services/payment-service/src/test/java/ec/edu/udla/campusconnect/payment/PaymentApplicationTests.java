package ec.edu.udla.campusconnect.payment;

import ec.edu.udla.campusconnect.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class PaymentApplicationTests {

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    void contextLoads() {
    }
}
