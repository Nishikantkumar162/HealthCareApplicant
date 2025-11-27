package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.DoctorResponse;
import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.entity.User;
import com.healthcare.appointment.enums.RoleName;
import com.healthcare.appointment.exception.ResourceNotFoundException;
import com.healthcare.appointment.repository.DoctorRepository;
import com.healthcare.appointment.repository.RoleRepository;
import com.healthcare.appointment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository,
                         UserRepository userRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<DoctorResponse> listDoctors(String specialization) {
        List<Doctor> doctors = specialization == null || specialization.isBlank()
                ? doctorRepository.findAll()
                : doctorRepository.findBySpecializationContainingIgnoreCaseAndApprovedTrue(specialization);

        return doctors.stream()
                .map(doc -> new DoctorResponse(
                        doc.getId(),
                        doc.getUser().getFullName(),
                        doc.getSpecialization(),
                        doc.getHospital(),
                        doc.isApproved()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setApproved(true);
        doctor.getUser().setEnabled(true);
        doctorRepository.save(doctor);
    }

    public DoctorResponse createDoctor(DoctorResponse dto, String email, String rawPassword) {
        User user = new User(dto.getFullName(), email,
                passwordEncoder.encode(rawPassword),
                roleRepository.findByName(RoleName.ROLE_DOCTOR)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userRepository.save(user);

        Doctor doctor = new Doctor(user, dto.getSpecialization(), dto.getHospital());
        doctor.setApproved(dto.isApproved());
        doctorRepository.save(doctor);
        return new DoctorResponse(doctor.getId(), user.getFullName(), doctor.getSpecialization(),
                doctor.getHospital(), doctor.isApproved());
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctorRepository.delete(doctor);
        userRepository.delete(doctor.getUser());
    }
}

