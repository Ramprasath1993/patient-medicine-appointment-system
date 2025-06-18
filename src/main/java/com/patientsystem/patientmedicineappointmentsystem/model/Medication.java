// src/main/java/com/patientsystem/patientmedicineappointmentsystem/model/Medication.java
package com.patientsystem.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String dosage;
}
