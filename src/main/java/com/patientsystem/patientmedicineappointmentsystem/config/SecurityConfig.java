// src/main/java/com/patientsystem/patientmedicineappointmentsystem/config/SecurityConfig.java
package com.patientsystem.patientmedicineappointmentsystem.config;

import com.patientsystem.patientmedicineappointmentsystem.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless API (or if handled by frontend)
                .csrf(AbstractHttpConfigurer::disable)
                // Enable CORS, Spring Security will automatically pick up the CorsConfigurationSource bean
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to registration and login API endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/logout", "/api/auth/check-session").permitAll()
                        // Allow public access to all static resources (HTML, CSS, JS) and the root path
                        .requestMatchers("/", "/index.html", "/register.html", "/dashboard.html",
                                "/book-appointment.html", "/view-medications.html",
                                "/style.css", "/script.js",
                                "/favicon.ico").permitAll() // Also include favicon if present
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                // Session management (Spring Security automatically handles session-based authentication)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use sessions
                        .maximumSessions(1) // Allow only one session per user
                        .maxSessionsPreventsLogin(false) // Allow new login to invalidate old session
                )
                .formLogin(AbstractHttpConfigurer::disable) // Disable default form login
                .httpBasic(AbstractHttpConfigurer::disable); // Disable basic auth

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Allow frontend origin. Adjust as needed if frontend is hosted elsewhere.
        // For local dev, adjust if you use live server (e.g., VS Code Live Server on 127.0.0.1:5500)
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:63342", "http://127.0.0.1:5500"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With", "remember-me"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}