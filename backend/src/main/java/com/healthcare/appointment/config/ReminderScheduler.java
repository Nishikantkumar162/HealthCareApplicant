package com.healthcare.appointment.config;

import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.enums.AppointmentStatus;
import com.healthcare.appointment.repository.AppointmentRepository;
import com.healthcare.appointment.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public ReminderScheduler(AppointmentRepository appointmentRepository,
                             NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sendUpcomingReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24h = now.plusHours(24);
        List<Appointment> upcoming = appointmentRepository.findAll().stream()
                .filter(app -> app.getStatus() == AppointmentStatus.CONFIRMED
                        && app.getAppointmentTime().isAfter(now)
                        && app.getAppointmentTime().isBefore(in24h))
                .toList();
        upcoming.forEach(app -> notificationService.sendEmail(
                app.getPatient().getUser().getEmail(),
                "Appointment Reminder",
                "Reminder: You have an appointment with " + app.getDoctor().getUser().getFullName() +
                        " at " + app.getAppointmentTime()
        ));
    }
}

