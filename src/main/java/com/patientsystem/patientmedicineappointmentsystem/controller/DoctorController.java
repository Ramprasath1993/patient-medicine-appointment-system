// src/main/java/com/patientsystem/patientmedicineappointmentsystem/controller/DoctorController.java
package com.patientsystem.patientmedicineappointmentsystem.controller;

import com.patientsystem.patientmedicineappointmentsystem.dto.DoctorDTO;
import com.patientsystem.patientmedicineappointmentsystem.model.Doctor;
import com.patientsystem.patientmedicineappointmentsystem.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Used for role-based authorization if implemented
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // This method would typically be for admin/privileged roles
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        try {
            Doctor doctor = convertToEntity(doctorDTO);
            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedDoctor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create doctor: " + e.getMessage());
        }
    }

    // This method would typically be for admin/privileged roles
    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        // Ensure the ID in the path matches the DTO ID if present
        if (doctorDTO.getId() != null && !doctorDTO.getId().equals(id)) {
            return ResponseEntity.badRequest().body("ID in path and body do not match.");
        }
        // For update, fetch existing and update fields, then save
        return doctorService.getDoctorById(id)
                .map(existingDoctor -> {
                    existingDoctor.setFirstName(doctorDTO.getFirstName());
                    existingDoctor.setLastName(doctorDTO.getLastName());
                    existingDoctor.setSpecialty(doctorDTO.getSpecialty());
                    existingDoctor.setContactNumber(doctorDTO.getContactNumber());
                    existingDoctor.setEmail(doctorDTO.getEmail()); // Be cautious with email updates
                    try {
                        Doctor updatedDoctor = doctorService.saveDoctor(existingDoctor);
                        return ResponseEntity.ok(convertToDto(updatedDoctor));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // This method would typically be for admin/privileged roles
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    // --- Helper methods for DTO conversion ---
    private DoctorDTO convertToDto(Doctor doctor) {
        return new DoctorDTO(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialty(), doctor.getContactNumber(), doctor.getEmail());
    }

    private Doctor convertToEntity(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDTO.getId()); // ID might be null for new entities
        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setSpecialty(doctorDTO.getSpecialty());
        doctor.setContactNumber(doctorDTO.getContactNumber());
        doctor.setEmail(doctorDTO.getEmail());
        return doctor;
    }
}
