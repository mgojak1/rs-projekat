package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Patient {
    int id;
    SimpleStringProperty name, surname, phoneNumber, livingPlace, diagnosis;
    SimpleDoubleProperty mass, height;
    SimpleObjectProperty<LocalDate> dateOfBirth;

    public Patient(int id, String name, String surname, String phoneNumber, String livingPlace, String diagnosis, double mass, double height, LocalDate dateOfBirth) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.livingPlace = new SimpleStringProperty(livingPlace);
        this.diagnosis = new SimpleStringProperty(diagnosis);
        this.mass = new SimpleDoubleProperty(mass);
        this.height = new SimpleDoubleProperty(height);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
    }

    public Patient() {
        PatientDAOBase patientDAOBase = PatientDAOBase.getInstance();
        this.id = patientDAOBase.getNextPatientID();
        this.name = new SimpleStringProperty("");
        this.surname = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.livingPlace = new SimpleStringProperty("");
        this.diagnosis = new SimpleStringProperty("");
        this.mass = new SimpleDoubleProperty(0);
        this.height = new SimpleDoubleProperty(0);
        this.dateOfBirth = new SimpleObjectProperty<>(LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public SimpleObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getDiagnosis() {
        return diagnosis.get();
    }

    public SimpleStringProperty diagnosisProperty() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis.set(diagnosis);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getLivingPlace() {
        return livingPlace.get();
    }

    public SimpleStringProperty livingPlaceProperty() {
        return livingPlace;
    }

    public void setLivingPlace(String livingPlace) {
        this.livingPlace.set(livingPlace);
    }

    public double getMass() {
        return mass.get();
    }

    public SimpleDoubleProperty massProperty() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass.set(mass);
    }

    public double getHeight() {
        return height.get();
    }

    public SimpleDoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname();
    }
}
