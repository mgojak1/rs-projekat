package ba.unsa.etf.rs.project;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientDAOBaseTest {

    @Test
    void regenerateFile() {
        PatientDAOBase.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        PatientDAOBase dao = PatientDAOBase.getInstance();
        ArrayList<Patient> patients = dao.getPatients();
        assertEquals("John", patients.get(0).getName());
        assertEquals("Evans", patients.get(1).getSurname());
    }

    @Test
    void deletePatient() {
        PatientDAOBase.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        PatientDAOBase dao = PatientDAOBase.getInstance();

        dao.deletePatient(dao.getPatients().get(1));

        ArrayList<Patient> patients = dao.getPatients();
        assertEquals(2, patients.size());
        assertEquals("John", patients.get(0).getName());
        assertEquals("Ronald", patients.get(1).getName());

        ArrayList<Appointment> appointments = dao.getAppointments();
        assertEquals(1,appointments.size());
    }

    @Test
    void addPatient() {
        PatientDAOBase.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        PatientDAOBase dao = PatientDAOBase.getInstance();
        Patient patient = new Patient();
        patient.setName("Test");
        patient.setSurname("Testy");
        patient.setDateOfBirth(LocalDate.now());
        patient.setDiagnosis("Clear");
        patient.setHeight(170);
        patient.setMass(60);
        patient.setLivingPlace("Washington DC");
        patient.setPhoneNumber("062/666-666");
        dao.addPatient(patient);

        ArrayList<Patient> patients = dao.getPatients();
        assertEquals("Testy", patients.get(3).getSurname());
    }

    @Test
    void addAppointment() {
        PatientDAOBase.removeInstance();
        File dbfile = new File("database.db");
        dbfile.delete();
        PatientDAOBase dao = PatientDAOBase.getInstance();

        Appointment appointment = new Appointment();
        appointment.setId(dao.getNextAppointmentID());
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTime(Time.valueOf(LocalTime.now()));
        appointment.setPatient(dao.getPatients().get(0));
        dao.addAppointment(appointment);

        ArrayList<Appointment> appointments = dao.getAppointments();
        assertEquals(3, appointments.size());
    }
}
