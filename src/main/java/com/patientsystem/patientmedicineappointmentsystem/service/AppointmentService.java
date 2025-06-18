// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/AppointmentService.java
// This file needs to be updated to integrate Doctor
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.Appointment;
import com.patientsystem.patientmedicineappointmentsystem.model.Patient;
import com.patientsystem.patientmedicineappointmentsystem.model.Doctor; // Import Doctor
import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.repository.AppointmentRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.PatientRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.UserRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.DoctorRepository; // Import DoctorRepository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository; // Inject DoctorRepository

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Appointment bookAppointment(Long userId, Long doctorId, LocalDate date, LocalTime time, String reason) { // Added doctorId parameter
        System.out.println("AppointmentService: Booking appointment for userId: " + userId + " with doctorId: " + doctorId);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            System.err.println("AppointmentService: ERROR: User not found for ID: " + userId);
            throw new IllegalArgumentException("User not found for the given ID: " + userId);
        }
        User user = userOptional.get();

        Patient patient = user.getPatient();
        if (patient == null) {
            System.err.println("AppointmentService: ERROR: Patient not associated with User ID: " + userId);
            throw new IllegalArgumentException("Patient details not found for the logged-in user.");
        }
        System.out.println("AppointmentService: Found Patient ID " + patient.getId() + " for User ID " + userId);

        // Fetch Doctor
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        if (doctorOptional.isEmpty()) {
            System.err.println("AppointmentService: ERROR: Doctor not found for ID: " + doctorId);
            throw new IllegalArgumentException("Doctor not found for the given ID: " + doctorId);
        }
        Doctor doctor = doctorOptional.get();
        System.out.println("AppointmentService: Found Doctor ID " + doctor.getId() + " for appointment.");

        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor); // Set the doctor
        newAppointment.setAppointmentDate(date);
        newAppointment.setAppointmentTime(time);
        newAppointment.setReason(reason);
        newAppointment.setStatus("SCHEDULED");

        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        System.out.println("AppointmentService: Appointment booked successfully with ID: " + savedAppointment.getId());
        return savedAppointment;
    }

    public List<Appointment> getAppointmentsByUserId(Long userId) {
        System.out.println("AppointmentService: Fetching appointments for userId: " + userId);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            System.err.println("AppointmentService: ERROR: User not found for ID: " + userId);
            throw new IllegalArgumentException("User not found for the given ID: " + userId);
        }
        User user = userOptional.get();

        Patient patient = user.getPatient();
        if (patient == null) {
            System.err.println("AppointmentService: ERROR: Patient not associated with User ID: " + userId + " when fetching appointments.");
            throw new IllegalArgumentException("Patient details not found for the logged-in user.");
        }
        System.out.println("AppointmentService: Found Patient ID " + patient.getId() + " for User ID " + userId + " when fetching appointments.");

        // Fetch appointments for this patient. Ensure lazy loading works or fetch doctor details explicitly if needed for frontend.
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patient.getId());
    }
}
