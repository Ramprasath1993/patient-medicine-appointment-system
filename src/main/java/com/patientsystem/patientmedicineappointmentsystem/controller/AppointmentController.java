// src/main/java/com/patientsystem/patientmedicineappointmentsystem/controller/AppointmentController.java
// This file needs to be updated to integrate Doctor
package com.patientsystem.patientmedicineappointmentsystem.controller;

import com.patientsystem.patientmedicineappointmentsystem.dto.AppointmentRequest;
import com.patientsystem.patientmedicineappointmentsystem.model.Appointment;
import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.service.AppointmentService;
import com.patientsystem.patientmedicineappointmentsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest appointmentRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            String username = authentication.getName();
            Long userId = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."))
                    .getId();

            Appointment newAppointment = appointmentService.bookAppointment(
                    userId,
                    appointmentRequest.getDoctorId(), // Pass doctorId
                    LocalDate.parse(appointmentRequest.getAppointmentDate()),
                    LocalTime.parse(appointmentRequest.getAppointmentTime()),
                    appointmentRequest.getReason()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book appointment: " + e.getMessage());
        }
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<?> getMyAppointments(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        try {
            String username = authentication.getName();
            Long userId = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."))
                    .getId();

            List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId);
            // Map to a cleaner DTO-like structure for frontend, including doctor's name
            List<Map<String, Object>> responseList = appointments.stream().map(app -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", app.getId());
                map.put("appointmentDate", app.getAppointmentDate());
                map.put("appointmentTime", app.getAppointmentTime());
                map.put("reason", app.getReason());
                map.put("status", app.getStatus());
                if (app.getDoctor() != null) {
                    map.put("doctorName", app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName());
                    map.put("doctorSpecialty", app.getDoctor().getSpecialty());
                } else {
                    map.put("doctorName", "N/A");
                    map.put("doctorSpecialty", "N/A");
                }
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(responseList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve appointments: " + e.getMessage());
        }
    }
}
