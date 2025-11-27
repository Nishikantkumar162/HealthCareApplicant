package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.*;
import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.entity.Role;
import com.healthcare.appointment.entity.User;
import com.healthcare.appointment.enums.RoleName;
import com.healthcare.appointment.exception.BadRequestException;
import com.healthcare.appointment.repository.DoctorRepository;
import com.healthcare.appointment.repository.PatientRepository;
import com.healthcare.appointment.repository.RoleRepository;
import com.healthcare.appointment.repository.UserRepository;
import com.healthcare.appointment.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       NotificationService notificationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.notificationService = notificationService;
    }

    public PatientResponse registerPatient(RegisterPatientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        Role role = roleRepository.findByName(RoleName.ROLE_PATIENT)
                .orElseThrow(() -> new BadRequestException("Patient role not configured"));

        User user = new User(request.getFullName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), role);
        userRepository.save(user);

        LocalDate dob = null;
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isBlank()) {
            dob = LocalDate.parse(request.getDateOfBirth());
        }
        Patient patient = new Patient(user, dob, request.getPhone(), request.getAddress());
        patientRepository.save(patient);

        notificationService.sendEmail(user.getEmail(), "Welcome to Healthcare System",
                "Hi " + user.getFullName() + ", your patient account is ready.");

        return new PatientResponse(patient.getId(), user.getFullName(), user.getEmail(), request.getPhone());
    }

    public DoctorResponse registerDoctor(RegisterDoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        Role role = roleRepository.findByName(RoleName.ROLE_DOCTOR)
                .orElseThrow(() -> new BadRequestException("Doctor role not configured"));

        User user = new User(request.getFullName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), role);
        user.setEnabled(false);
        userRepository.save(user);

        Doctor doctor = new Doctor(user, request.getSpecialization(), request.getHospital());
        doctorRepository.save(doctor);

        notificationService.sendEmail(user.getEmail(), "Doctor registration received",
                "Hi " + user.getFullName() + ", your account is pending admin approval.");

        return new DoctorResponse(doctor.getId(), user.getFullName(), doctor.getSpecialization(),
                doctor.getHospital(), doctor.isApproved());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        return new AuthResponse(token, user.getRole().getName().name(), user.getId(), user.getFullName());
    }
}

