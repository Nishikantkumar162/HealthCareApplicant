package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.PatientResponse;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.exception.ResourceNotFoundException;
import com.healthcare.appointment.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponse> listPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patient -> new PatientResponse(
                        patient.getId(),
                        patient.getUser().getFullName(),
                        patient.getUser().getEmail(),
                        patient.getPhone()))
                .collect(Collectors.toList());
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }
}

