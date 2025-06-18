// src/main/java/com/patientsystem/patientmedicineappointmentsystem/controller/PrescriptionController.java
package com.patientsystem.patientmedicineappointmentsystem.controller;

import com.patientsystem.patientmedicineappointmentsystem.dto.PrescriptionRequest;
import com.patientsystem.patientmedicineappointmentsystem.model.Prescription;
import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.service.PrescriptionService;
import com.patientsystem.patientmedicineappointmentsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final UserService userService; // To get patient ID from authenticated user

    public PrescriptionController(PrescriptionService prescriptionService, UserService userService) {
        this.prescriptionService = prescriptionService;
        this.userService = userService;
    }

    // For patients to view their own prescriptions
    @GetMapping("/my-prescriptions")
    public ResponseEntity<?> getMyPrescriptions(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

            if (currentUser.getPatient() == null) {
                return ResponseEntity.badRequest().body("Patient details not found for this user.");
            }
            Long patientId = currentUser.getPatient().getId();

            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
            // Map to DTOs for cleaner output, or include full nested objects if desired by frontend
            List<Map<String, Object>> responseList = prescriptions.stream().map(p -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", p.getId());
                map.put("medicationName", p.getMedication().getName());
                map.put("medicationDosage", p.getMedication().getDosage());
                map.put("medicationDescription", p.getMedication().getDescription());
                map.put("dosage", p.getDosage());
                map.put("frequency", p.getFrequency());
                map.put("startDate", p.getStartDate());
                map.put("endDate", p.getEndDate());
                map.put("notes", p.getNotes());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(responseList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve prescriptions: " + e.getMessage());
        }
    }

    // This method would typically be for admin/doctor roles to create prescriptions
    // For simplicity, we expose it, but in a real app, it would be secured.
    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequest prescriptionRequest) {
        try {
            LocalDate startDate = LocalDate.parse(prescriptionRequest.getStartDate());
            LocalDate endDate = prescriptionRequest.getEndDate() != null && !prescriptionRequest.getEndDate().isEmpty()
                    ? LocalDate.parse(prescriptionRequest.getEndDate()) : null;

            Prescription newPrescription = prescriptionService.createPrescription(
                    prescriptionRequest.getPatientId(),
                    prescriptionRequest.getMedicationId(),
                    prescriptionRequest.getDosage(),
                    prescriptionRequest.getFrequency(),
                    startDate,
                    endDate,
                    prescriptionRequest.getNotes()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newPrescription); // You might want a DTO here
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create prescription: " + e.getMessage());
        }
    }
}

