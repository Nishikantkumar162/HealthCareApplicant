package com.healthcare.appointment.config;

import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.entity.Role;
import com.healthcare.appointment.entity.User;
import com.healthcare.appointment.enums.RoleName;
import com.healthcare.appointment.repository.DoctorRepository;
import com.healthcare.appointment.repository.PatientRepository;
import com.healthcare.appointment.repository.RoleRepository;
import com.healthcare.appointment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName)));
            }

            userRepository.findByEmail("admin@healthcare.com").orElseGet(() -> {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
                User admin = new User("System Admin", "admin@healthcare.com",
                        passwordEncoder.encode("Admin@123"), adminRole);
                admin.setEnabled(true);
                log.info("Creating default admin user: admin@healthcare.com / Admin@123");
                return userRepository.save(admin);
            });

            if (doctorRepository.count() == 0) {
                Role doctorRole = roleRepository.findByName(RoleName.ROLE_DOCTOR).orElseThrow();
                User doctorUser = new User("Dr. Strange", "doctor@healthcare.com",
                        passwordEncoder.encode("Doctor@123"), doctorRole);
                doctorUser.setEnabled(true);
                userRepository.save(doctorUser);
                Doctor doctor = new Doctor(doctorUser, "Cardiology", "City Hospital");
                doctor.setApproved(true);
                doctorRepository.save(doctor);
            }

            if (patientRepository.count() == 0) {
                Role patientRole = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseThrow();
                User patientUser = new User("John Patient", "patient@healthcare.com",
                        passwordEncoder.encode("Patient@123"), patientRole);
                patientUser.setEnabled(true);
                userRepository.save(patientUser);
                patientRepository.save(new Patient(patientUser, null, "9999999999", "123 Main St"));
            }
        };
    }
}

