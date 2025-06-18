// src/main/java/com/patientsystem/patientmedicineappointmentsystem/repository/AppointmentRepository.java
package com.patientsystem.patientmedicineappointmentsystem.repository;

import com.patientsystem.patientmedicineappointmentsystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);
}