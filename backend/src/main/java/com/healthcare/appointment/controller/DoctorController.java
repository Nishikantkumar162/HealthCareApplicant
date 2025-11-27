package com.healthcare.appointment.controller;

import com.healthcare.appointment.dto.AppointmentResponse;
import com.healthcare.appointment.dto.AvailabilityRequest;
import com.healthcare.appointment.dto.UpdateAppointmentStatusRequest;
import com.healthcare.appointment.entity.User;
import com.healthcare.appointment.service.AppointmentService;
import com.healthcare.appointment.service.AvailabilityService;
import com.healthcare.appointment.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;
    private final UserContextService userContextService;

    public DoctorController(AppointmentService appointmentService,
                            AvailabilityService availabilityService,
                            UserContextService userContextService) {
        this.appointmentService = appointmentService;
        this.availabilityService = availabilityService;
        this.userContextService = userContextService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> myAppointments() {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(appointmentService.getAppointmentsForDoctor(user.getId()));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id,
                                                            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(appointmentService.updateStatus(user.getId(), id, request.getStatus(), request.getNotes()));
    }

    @PostMapping("/availability")
    public ResponseEntity<?> addAvailability(@Valid @RequestBody AvailabilityRequest request) {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(availabilityService.addAvailability(user.getId(), request));
    }

    @GetMapping("/availability")
    public ResponseEntity<?> listAvailability() {
        User user = userContextService.getCurrentUser();
        return ResponseEntity.ok(availabilityService.getDoctorAvailability(user.getId()));
    }
}

