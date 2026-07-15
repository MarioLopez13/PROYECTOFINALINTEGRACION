package ec.edu.udla.campusconnect.attendance;

import ec.edu.udla.campusconnect.attendance.repository.AttendanceRecordRepository;
import ec.edu.udla.campusconnect.attendance.repository.IncidentReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class AttendanceApplicationTests {

    @MockBean
    private AttendanceRecordRepository attendanceRecordRepository;

    @MockBean
    private IncidentReportRepository incidentReportRepository;

    @Test
    void contextLoads() {
    }
}
