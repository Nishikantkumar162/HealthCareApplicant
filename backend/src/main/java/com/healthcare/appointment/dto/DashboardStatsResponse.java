package com.healthcare.appointment.dto;

public class DashboardStatsResponse {
    private long totalPatients;
    private long totalDoctors;
    private long pendingAppointments;
    private long confirmedAppointments;

    public DashboardStatsResponse(long totalPatients, long totalDoctors, long pendingAppointments, long confirmedAppointments) {
        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.pendingAppointments = pendingAppointments;
        this.confirmedAppointments = confirmedAppointments;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public long getConfirmedAppointments() {
        return confirmedAppointments;
    }
}

