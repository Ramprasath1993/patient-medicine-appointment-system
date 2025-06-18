// src/main/java/com/patientsystem/patientmedicineappointmentsystem/repository/PrescriptionRepository.java
package com.patientsystem.patientmedicineappointmentsystem.repository;

import com.patientsystem.patientmedicineappointmentsystem.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
    // You might add more specific queries later, e.g., by patientId and medicationId
}
