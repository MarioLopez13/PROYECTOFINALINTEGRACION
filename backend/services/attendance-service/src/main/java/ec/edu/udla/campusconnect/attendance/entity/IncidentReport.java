package ec.edu.udla.campusconnect.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "incident_reports")
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String studentCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IncidentSeverity severity;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 600)
    private String description;

    @Column(nullable = false, length = 80)
    private String reportedBy;

    @Column(nullable = false)
    private OffsetDateTime reportedAt;

    protected IncidentReport() {
    }

    private IncidentReport(String studentCode, IncidentSeverity severity, String title,
                           String description, String reportedBy, OffsetDateTime reportedAt) {
        this.studentCode = studentCode;
        this.severity = severity;
        this.title = title;
        this.description = description;
        this.reportedBy = reportedBy;
        this.reportedAt = reportedAt;
    }

    public static IncidentReport create(String studentCode, IncidentSeverity severity, String title,
                                        String description, String reportedBy, OffsetDateTime reportedAt) {
        return new IncidentReport(studentCode, severity, title, description, reportedBy, reportedAt);
    }

    @PrePersist
    void prePersist() {
        if (reportedAt == null) {
            reportedAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public OffsetDateTime getReportedAt() {
        return reportedAt;
    }
}
