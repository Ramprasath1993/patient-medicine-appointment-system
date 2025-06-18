// src/main/java/com/patientsystem/patientmedicineappointmentsystem/repository/UserRepository.java
package com.patientsystem.patientmedicineappointmentsystem.repository;

import com.patientsystem.patientmedicineappointmentsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}