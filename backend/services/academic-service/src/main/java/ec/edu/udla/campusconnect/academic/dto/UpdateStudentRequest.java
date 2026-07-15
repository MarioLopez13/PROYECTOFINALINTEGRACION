package ec.edu.udla.campusconnect.academic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateStudentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull @Past LocalDate birthDate,
        @NotBlank @Email String representativeEmail,
        @NotBlank String representativePhone
) {
}
