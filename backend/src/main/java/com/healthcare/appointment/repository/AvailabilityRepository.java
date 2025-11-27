package com.healthcare.appointment.repository;

import com.healthcare.appointment.entity.Availability;
import com.healthcare.appointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByDoctor(Doctor doctor);
    List<Availability> findByDoctorAndSpecificDate(Doctor doctor, LocalDate date);
    List<Availability> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}

