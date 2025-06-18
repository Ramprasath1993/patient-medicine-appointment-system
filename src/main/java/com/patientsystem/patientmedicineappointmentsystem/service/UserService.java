// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/UserService.java
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.model.Patient;
import com.patientsystem.patientmedicineappointmentsystem.repository.PatientRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(String username, String password, String firstName, String lastName, String dateOfBirth, String contactNumber) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Encode password
        newUser.setRole("PATIENT");

        // Save user first to get ID
        newUser = userRepository.save(newUser);

        Patient newPatient = new Patient();
        newPatient.setUser(newUser); // Link the patient to the user
        newPatient.setFirstName(firstName);
        newPatient.setLastName(lastName);
        // Handle dateOfBirth string to LocalDate conversion if necessary
        // For simplicity, directly setting here. In a real app, use a formatter or DTO.
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            newPatient.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        }
        newPatient.setContactNumber(contactNumber);

        patientRepository.save(newPatient);
        return newUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}