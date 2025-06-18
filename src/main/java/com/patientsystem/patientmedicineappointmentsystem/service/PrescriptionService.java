// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/PrescriptionService.java
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.Medication;
import com.patientsystem.patientmedicineappointmentsystem.model.Patient;
import com.patientsystem.patientmedicineappointmentsystem.model.Prescription;
import com.patientsystem.patientmedicineappointmentsystem.repository.MedicationRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.PatientRepository;
import com.patientsystem.patientmedicineappointmentsystem.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, MedicationRepository medicationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public Prescription createPrescription(Long patientId, Long medicationId, String dosage, String frequency, LocalDate startDate, LocalDate endDate, String notes) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }
        Patient patient = patientOptional.get();

        Optional<Medication> medicationOptional = medicationRepository.findById(medicationId);
        if (medicationOptional.isEmpty()) {
            throw new IllegalArgumentException("Medication not found with ID: " + medicationId);
        }
        Medication medication = medicationOptional.get();

        Prescription newPrescription = new Prescription();
        newPrescription.setPatient(patient);
        newPrescription.setMedication(medication);
        newPrescription.setDosage(dosage);
        newPrescription.setFrequency(frequency);
        newPrescription.setStartDate(startDate);
        newPrescription.setEndDate(endDate);
        newPrescription.setNotes(notes);

        return prescriptionRepository.save(newPrescription);
    }

    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Transactional
    public Prescription updatePrescription(Long id, String dosage, String frequency, LocalDate startDate, LocalDate endDate, String notes) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with ID: " + id));

        existingPrescription.setDosage(dosage);
        existingPrescription.setFrequency(frequency);
        existingPrescription.setStartDate(startDate);
        existingPrescription.setEndDate(endDate);
        existingPrescription.setNotes(notes);

        return prescriptionRepository.save(existingPrescription);
    }

    @Transactional
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}

