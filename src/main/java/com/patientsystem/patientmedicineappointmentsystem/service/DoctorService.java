
// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/DoctorService.java
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.Doctor;
import com.patientsystem.patientmedicineappointmentsystem.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAllByOrderByLastNameAscFirstNameAsc();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        // Basic validation for new doctors to prevent duplicate emails
        if (doctor.getId() == null && doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Doctor with this email already exists.");
        }
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
