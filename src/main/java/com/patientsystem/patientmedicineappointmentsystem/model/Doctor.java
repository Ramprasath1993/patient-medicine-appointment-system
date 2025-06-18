// src/main/java/com/patientsystem/patientmedicineappointmentsystem/model/Doctor.java
package com.patientsystem.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set; // For one-to-many relationship with appointments

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String specialty;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(unique = true)
    private String email;

    // Optional: If you want to list appointments associated with a doctor
    // This side is the "owning" side in terms of adding new appointments.
    // However, for fetching doctor's schedule, you might fetch all appointments for a doctor.
    // For now, we'll keep it simple and manage appointments primarily from the patient side.
    // @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Appointment> appointments;
}
