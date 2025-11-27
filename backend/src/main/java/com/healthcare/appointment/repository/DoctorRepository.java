package com.healthcare.appointment.repository;

import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByApprovedTrue();
    List<Doctor> findBySpecializationContainingIgnoreCaseAndApprovedTrue(String specialization);
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUserId(Long userId);
}

