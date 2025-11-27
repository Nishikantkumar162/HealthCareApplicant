package com.healthcare.appointment.dto;

public class DoctorResponse {
    private Long id;
    private String fullName;
    private String specialization;
    private String hospital;
    private boolean approved;

    public DoctorResponse(Long id, String fullName, String specialization, String hospital, boolean approved) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.hospital = hospital;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getHospital() {
        return hospital;
    }

    public boolean isApproved() {
        return approved;
    }
}

