// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/MedicationService.java
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.Medication;
import com.patientsystem.patientmedicineappointmentsystem.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }


}
