package com.healthcare.appointment.controller;

import com.healthcare.appointment.dto.DoctorResponse;
import com.healthcare.appointment.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin
public class PublicController {

    private final DoctorService doctorService;

    public PublicController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(doctorService.listDoctors(specialization));
    }
}

