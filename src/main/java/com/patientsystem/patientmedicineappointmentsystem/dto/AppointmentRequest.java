// src/main/java/com/patientsystem/patientmedicineappointmentsystem/dto/AppointmentRequest.java
// This file needs to be updated to include doctorId
package com.patientsystem.patientmedicineappointmentsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @NotNull(message = "Doctor ID is required")
    private Long doctorId; // NEW: Doctor ID
    @NotBlank(message = "Appointment date is required")
    private String appointmentDate; // YYYY-MM-DD
    @NotBlank(message = "Appointment time is required")
    private String appointmentTime; // HH:MM
    private String reason;
}
