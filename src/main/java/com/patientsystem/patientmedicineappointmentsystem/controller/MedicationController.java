// src/main/java/com/patientsystem/patientmedicineappointmentsystem/controller/MedicationController.java
package com.patientsystem.patientmedicineappointmentsystem.controller;

import com.patientsystem.patientmedicineappointmentsystem.model.Medication;
import com.patientsystem.patientmedicineappointmentsystem.service.MedicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> medications = medicationService.getAllMedications();
        return ResponseEntity.ok(medications);
    }
}
