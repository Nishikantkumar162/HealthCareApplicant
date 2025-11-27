package com.healthcare.appointment.controller;

import com.healthcare.appointment.dto.*;
import com.healthcare.appointment.service.AppointmentService;
import com.healthcare.appointment.service.DoctorService;
import com.healthcare.appointment.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public AdminController(DoctorService doctorService,
                           PatientService patientService,
                           AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> dashboard() {
        return ResponseEntity.ok(appointmentService.getDashboardStats());
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> appointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> doctors(@RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(doctorService.listDoctors(specialization));
    }

    @PutMapping("/doctors/{id}/approve")
    public ResponseEntity<Void> approveDoctor(@PathVariable Long id) {
        doctorService.approveDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> patients() {
        return ResponseEntity.ok(patientService.listPatients());
    }
}

