package ba.unsa.etf.rs.project;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Appointment {
    int id;
    LocalDate appointmentDate;
    Time appointmentTime;
    Patient patient;

    public Appointment(int id, Timestamp appointmentDate, Patient patient) {
        this.id = id;
        this.appointmentDate = appointmentDate.toLocalDateTime().toLocalDate();
        this.appointmentTime = Time.valueOf(appointmentDate.toLocalDateTime().toLocalTime().minusHours(2)); //Time offset by 2h??
        this.patient = patient;
    }

    public Appointment() {
    }

    public int getId() {
        return id;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}