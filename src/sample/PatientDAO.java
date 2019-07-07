package sample;

public interface PatientDAO {
    Patient getPatient();
    void addPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(Patient patient);
    void close();
}
