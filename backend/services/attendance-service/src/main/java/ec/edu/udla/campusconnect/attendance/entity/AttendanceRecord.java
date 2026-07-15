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

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String studentCode;

    @Column(nullable = false)
    private LocalDate classDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceType type;

    @Column(nullable = false, length = 80)
    private String recordedBy;

    @Column(length = 300)
    private String notes;

    @Column(nullable = false)
    private OffsetDateTime recordedAt;

    protected AttendanceRecord() {
    }

    private AttendanceRecord(String studentCode, LocalDate classDate, AttendanceType type,
                             String recordedBy, String notes) {
        this.studentCode = studentCode;
        this.classDate = classDate;
        this.type = type;
        this.recordedBy = recordedBy;
        this.notes = notes;
    }

    public static AttendanceRecord create(String studentCode, LocalDate classDate, AttendanceType type,
                                          String recordedBy, String notes) {
        return new AttendanceRecord(studentCode, classDate, type, recordedBy, notes);
    }

    @PrePersist
    void prePersist() {
        recordedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public AttendanceType getType() {
        return type;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public String getNotes() {
        return notes;
    }

    public OffsetDateTime getRecordedAt() {
        return recordedAt;
    }
}
