// src/main/java/com/patientsystem/patientmedicineappointmentsystem/dto/DoctorDTO.java
package com.patientsystem.patientmedicineappointmentsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String contactNumber;
    private String email;
}