// src/main/java/com/patientsystem/patientmedicineappointmentsystem/dto/LoginRequest.java
package com.patientsystem.patientmedicineappointmentsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}