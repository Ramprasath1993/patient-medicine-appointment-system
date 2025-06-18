// src/main/java/com/patientsystem/patientmedicineappointmentsystem/repository/PatientRepository.java
package com.patientsystem.patientmedicineappointmentsystem.repository;

import com.patientsystem.patientmedicineappointmentsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserId(Long userId);
}
