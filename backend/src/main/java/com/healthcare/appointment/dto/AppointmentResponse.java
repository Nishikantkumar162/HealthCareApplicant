package com.healthcare.appointment.dto;

import com.healthcare.appointment.enums.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
    private String reason;
    private String notes;

    public AppointmentResponse(Long id, Long patientId, Long doctorId, String doctorName, String patientName,
                               LocalDateTime appointmentTime, AppointmentStatus status, String reason, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getNotes() {
        return notes;
    }
}

