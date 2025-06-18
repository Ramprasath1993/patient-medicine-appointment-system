// src/main/java/com/patientsystem/patientmedicineappointmentsystem/repository/DoctorRepository.java
package com.patientsystem.patientmedicineappointmentsystem.repository;

import com.patientsystem.patientmedicineappointmentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByOrderByLastNameAscFirstNameAsc();
    Optional<Doctor> findByEmail(String email);
}