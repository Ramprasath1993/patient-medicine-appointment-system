// src/main/java/com/patientsystem/patientmedicineappointmentsystem/model/Appointment.java
// This file needs to be updated to include a Doctor reference
package com.patientsystem.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // NEW: Link to Doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor; // Each appointment is for a specific doctor

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private String status = "SCHEDULED"; // e.g., SCHEDULED, COMPLETED, CANCELLED
}
