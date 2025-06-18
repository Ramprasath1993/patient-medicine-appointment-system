// src/main/java/com/patientsystem/patientmedicineappointmentsystem/dto/PrescriptionRequest.java
package com.patientsystem.patientmedicineappointmentsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId; // This would typically be derived from the authenticated user, but useful for admin if exposed.
    @NotNull(message = "Medication ID is required")
    private Long medicationId;
    @NotBlank(message = "Dosage is required")
    private String dosage;
    @NotBlank(message = "Frequency is required")
    private String frequency;
    @NotNull(message = "Start date is required")
    private String startDate; // As String to parse LocalDate in service
    private String endDate; // As String to parse LocalDate in service
    private String notes;
}
