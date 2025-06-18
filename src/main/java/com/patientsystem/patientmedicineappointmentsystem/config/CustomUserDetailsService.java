// CustomUserDetailsService.java remains the same
// src/main/java/com/patientsystem/patientmedicineappointmentsystem/service/CustomUserDetailsService.java
package com.patientsystem.patientmedicineappointmentsystem.service;

import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Return Spring Security's UserDetails object
        // For simplicity, using empty authorities. In a real app, define roles.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
