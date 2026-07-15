package ec.edu.udla.campusconnect.academic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 30)
    private String schoolId;

    @Column(nullable = false, length = 40)
    private String grade;

    @Column(nullable = false, length = 20)
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pendingAmount;

    @Column(nullable = false)
    private OffsetDateTime enrolledAt;

    protected Enrollment() {
    }

    private Enrollment(Student student, String schoolId, String grade, String academicYear, BigDecimal pendingAmount) {
        this.student = student;
        this.schoolId = schoolId;
        this.grade = grade;
        this.academicYear = academicYear;
        this.pendingAmount = pendingAmount;
        this.status = EnrollmentStatus.ENROLLED;
    }

    public static Enrollment create(Student student, String schoolId, String grade, String academicYear, BigDecimal pendingAmount) {
        return new Enrollment(student, schoolId, grade, academicYear, pendingAmount);
    }

    @PrePersist
    void prePersist() {
        enrolledAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getGrade() {
        return grade;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }

    public OffsetDateTime getEnrolledAt() {
        return enrolledAt;
    }
}
