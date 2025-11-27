package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.AvailabilityRequest;
import com.healthcare.appointment.entity.Availability;
import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.exception.ResourceNotFoundException;
import com.healthcare.appointment.repository.AvailabilityRepository;
import com.healthcare.appointment.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository,
                               DoctorRepository doctorRepository) {
        this.availabilityRepository = availabilityRepository;
        this.doctorRepository = doctorRepository;
    }

    public Availability addAvailability(Long doctorUserId, AvailabilityRequest request) {
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Availability availability = new Availability(
                doctor,
                request.getDayOfWeek(),
                request.getSpecificDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getMaxSlots()
        );
        return availabilityRepository.save(availability);
    }

    public List<Availability> getDoctorAvailability(Long doctorUserId) {
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return availabilityRepository.findByDoctor(doctor);
    }
}

