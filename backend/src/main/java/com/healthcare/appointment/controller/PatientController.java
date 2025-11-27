package com.healthcare.appointment.controller;

import com.healthcare.appointment.dto.AppointmentRequest;
import com.healthcare.appointment.dto.AppointmentResponse;
import com.healthcare.appointment.dto.RescheduleRequest;
import com.healthcare.appointment.entity.User;
import com.healthcare.appointment.service.AppointmentService;
import com.healthcare.appointment.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final AppointmentService appointmentService;
    private final UserContextService userContextService;

    public PatientController(AppointmentService appointmentService,
                             UserContextService userContextService) {
        this.appointmentService = appointmentService;
        this.userContextService = userContextService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> myAppointments() {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(appointmentService.getAppointmentsForPatient(user.getId()));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest request) {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(appointmentService.bookAppointment(user.getId(), request));
    }

    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        User user = userContextService.getCurrentUser();
        appointmentService.cancelAppointment(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/appointments/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(@PathVariable Long id,
                                                          @Valid @RequestBody RescheduleRequest request) {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(user.getId(), id, request.getNewTime()));
    }
}

