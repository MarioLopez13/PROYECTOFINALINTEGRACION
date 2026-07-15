package ec.edu.udla.campusconnect.academic;

import ec.edu.udla.campusconnect.academic.repository.EnrollmentRepository;
import ec.edu.udla.campusconnect.academic.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class AcademicApplicationTests {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private EnrollmentRepository enrollmentRepository;

    @Test
    void contextLoads() {
    }
}
