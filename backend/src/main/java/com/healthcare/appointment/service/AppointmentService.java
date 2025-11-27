package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.AppointmentRequest;
import com.healthcare.appointment.dto.AppointmentResponse;
import com.healthcare.appointment.dto.DashboardStatsResponse;
import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.entity.Doctor;
import com.healthcare.appointment.entity.Patient;
import com.healthcare.appointment.enums.AppointmentStatus;
import com.healthcare.appointment.exception.BadRequestException;
import com.healthcare.appointment.exception.ResourceNotFoundException;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.appointment.repository.DoctorRepository;
import com.healthcare.appointment.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.notificationService = notificationService;
    }

    public AppointmentResponse bookAppointment(Long patientUserId, AppointmentRequest request) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (!doctor.isApproved()) {
            throw new BadRequestException("Doctor not yet approved");
        }
        if (appointmentRepository.existsByDoctorAndAppointmentTime(doctor, request.getAppointmentTime())) {
            throw new BadRequestException("Slot already booked");
        }

        Appointment appointment = new Appointment(patient, doctor, request.getAppointmentTime(), request.getReason());
        appointmentRepository.save(appointment);

        notificationService.sendEmail(doctor.getUser().getEmail(), "New appointment request",
                "New appointment from " + patient.getUser().getFullName() + " at " + request.getAppointmentTime());
        notificationService.sendEmail(patient.getUser().getEmail(), "Appointment request received",
                "Doctor " + doctor.getUser().getFullName() + " will confirm shortly.");

        return mapToResponse(appointment);
    }

    public List<AppointmentResponse> getAppointmentsForPatient(Long patientUserId) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return appointmentRepository.findByPatient(patient)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsForDoctor(Long doctorUserId) {
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository.findByDoctor(doctor)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse updateStatus(Long doctorUserId, Long appointmentId, AppointmentStatus status, String notes) {
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException("Cannot update appointments for another doctor");
        }
        appointment.setStatus(status);
        appointment.setNotes(notes);
        appointmentRepository.save(appointment);

        notificationService.sendEmail(appointment.getPatient().getUser().getEmail(),
                "Appointment updated",
                "Your appointment status is now " + status.name());

        return mapToResponse(appointment);
    }

    public void cancelAppointment(Long patientUserId, Long appointmentId) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new BadRequestException("Cannot cancel another patient's appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        notificationService.sendEmail(appointment.getDoctor().getUser().getEmail(),
                "Appointment cancelled",
                "Appointment #" + appointment.getId() + " has been cancelled by the patient.");
    }

    public AppointmentResponse rescheduleAppointment(Long patientUserId, Long appointmentId, java.time.LocalDateTime newTime) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new BadRequestException("Cannot reschedule another patient's appointment");
        }
        if (appointmentRepository.existsByDoctorAndAppointmentTime(appointment.getDoctor(), newTime)) {
            throw new BadRequestException("Slot already taken");
        }
        appointment.setAppointmentTime(newTime);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);

        notificationService.sendEmail(appointment.getDoctor().getUser().getEmail(),
                "Appointment rescheduled",
                "Appointment #" + appointment.getId() + " is now scheduled for " + newTime);
        return mapToResponse(appointment);
    }

    public DashboardStatsResponse getDashboardStats() {
        long pending = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        long confirmed = appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED);
        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        return new DashboardStatsResponse(totalPatients, totalDoctors, pending, confirmed);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getUser().getFullName(),
                appointment.getPatient().getUser().getFullName(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getNotes()
        );
    }
}

