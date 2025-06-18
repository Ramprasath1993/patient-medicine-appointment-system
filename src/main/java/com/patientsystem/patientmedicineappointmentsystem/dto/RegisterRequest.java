// src/main/java/com/patientsystem/patientmedicineappointmentsystem/dto/RegisterRequest.java
package com.patientsystem.patientmedicineappointmentsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth; // Use String for simplicity, parse in service
    private String contactNumber;
}