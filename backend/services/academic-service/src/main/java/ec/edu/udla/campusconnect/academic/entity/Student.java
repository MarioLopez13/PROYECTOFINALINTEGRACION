package ec.edu.udla.campusconnect.academic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String studentCode;

    @Column(nullable = false, unique = true, length = 30)
    private String documentNumber;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 120)
    private String representativeEmail;

    @Column(nullable = false, length = 30)
    private String representativePhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudentStatus status;

    @Column(nullable = false, length = 30)
    private String financialStatus;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    protected Student() {
    }

    private Student(String studentCode, String documentNumber, String firstName, String lastName,
                    LocalDate birthDate, String representativeEmail, String representativePhone) {
        this.studentCode = studentCode;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.representativeEmail = representativeEmail;
        this.representativePhone = representativePhone;
        this.status = StudentStatus.ACTIVE;
        this.financialStatus = "PENDING";
    }

    public static Student create(String studentCode, String documentNumber, String firstName, String lastName,
                                 LocalDate birthDate, String representativeEmail, String representativePhone) {
        return new Student(studentCode, documentNumber, firstName, lastName, birthDate, representativeEmail, representativePhone);
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public void updateProfile(String firstName, String lastName, LocalDate birthDate,
                              String representativeEmail, String representativePhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.representativeEmail = representativeEmail;
        this.representativePhone = representativePhone;
    }

    public void markInactive() {
        this.status = StudentStatus.INACTIVE;
    }

    public void markFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    public Long getId() {
        return id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getRepresentativeEmail() {
        return representativeEmail;
    }

    public String getRepresentativePhone() {
        return representativePhone;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public String getFinancialStatus() {
        return financialStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
