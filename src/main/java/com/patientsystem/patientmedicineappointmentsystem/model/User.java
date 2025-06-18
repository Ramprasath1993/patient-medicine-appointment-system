// src/main/java/com/patientsystem/patientmedicineappointmentsystem/model/User.java
package com.patientsystem.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // This will store BCrypt encoded password

    @Column(nullable = false)
    private String role = "PATIENT"; // Default role

    // Crucial for the bi-directional relationship and cascading saves
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Patient patient; // Link to patient details

    // Ensure you have getters and setters, Lombok's @Data handles this.
}
