// src/main/java/com/patientsystem/patientmedicineappointmentsystem/controller/AuthController.java
package com.patientsystem.patientmedicineappointmentsystem.controller;

import com.patientsystem.patientmedicineappointmentsystem.dto.LoginRequest;
import com.patientsystem.patientmedicineappointmentsystem.dto.RegisterRequest;
import com.patientsystem.patientmedicineappointmentsystem.model.User;
import com.patientsystem.patientmedicineappointmentsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = userService.registerNewUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getDateOfBirth(),
                    registerRequest.getContactNumber()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful for user: " + newUser.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

            // Fetch user details including ID to return
            User loggedInUser = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful!");
            responseBody.put("userId", loggedInUser.getId());
            responseBody.put("username", loggedInUser.getUsername());

            return ResponseEntity.ok().body(responseBody); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid credentials.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        SecurityContextHolder.clearContext(); // Clear security context
        return ResponseEntity.ok().body("Logged out successfully.");
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Session active.");
            responseBody.put("userId", currentUser.getId());
            responseBody.put("username", currentUser.getUsername());
            return ResponseEntity.ok().body(responseBody);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session.");
        }
    }
}
