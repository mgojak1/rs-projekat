package ba.unsa.etf.rs.project;

public interface PatientDAO {
    void addPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(Patient patient);
    void close();
}
