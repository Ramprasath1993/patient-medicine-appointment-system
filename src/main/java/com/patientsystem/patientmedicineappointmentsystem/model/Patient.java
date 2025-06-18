// src/main/java/com/patientsystem/patientmedicineappointmentsystem/model/Patient.java
package com.patientsystem.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This links the patient to the user through the user_id column
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true) // user_id column in patients table
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "contact_number")
    private String contactNumber;

    // Helper method to link user to patient bi-directionally
    // This is important when setting the user on a new Patient object.
    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getPatient() != this) { // Avoid infinite loop
            user.setPatient(this);
        }
    }
}
