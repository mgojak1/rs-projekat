package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;

public class Controller {
    public TableView<Appointment> tableAppointments;
    public TableColumn colAppointmentNumber;
    public TableColumn<Appointment, String> colAppointmentPatient;
    public TableColumn colAppointmentDate;
    public TableColumn colAppointmentTime;
    public ListView<Patient> listPatients;
    public TextField fieldPatientName;
    public TextField fieldPatientSurname;
    public TextField fieldPhoneNumber;
    public TextField fieldLivingPlace;
    public TextField fieldMass;
    public TextField fieldHeight;
    public DatePicker fieldDateOfBirth;
    public TextArea areaDiagnosis;
    private PatientDAOBase dao;
    private ObservableList<Patient> patientsList;
    private ObservableList<Appointment> appointmentsList;

    public Controller() {
        dao = PatientDAOBase.getInstance();
        patientsList = FXCollections.observableArrayList(dao.getPatients());
        appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
    }

    @FXML
    public void initialize() {
        listPatients.setItems(patientsList);

        tableAppointments.setItems(appointmentsList);
        colAppointmentNumber.setCellValueFactory(new PropertyValueFactory("id"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory("appointmentDate"));
        colAppointmentTime.setCellValueFactory(new PropertyValueFactory("appointmentTime"));
        colAppointmentPatient.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPatient().toString()));

        listPatients.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(oldValue != null) {
                fieldPatientName.textProperty().unbindBidirectional(oldValue.nameProperty());
                fieldPatientSurname.textProperty().unbindBidirectional(oldValue.surnameProperty());
                fieldDateOfBirth.valueProperty().unbindBidirectional(oldValue.dateOfBirthProperty());
                fieldMass.textProperty().unbindBidirectional(oldValue.massProperty());
                fieldHeight.textProperty().unbindBidirectional(oldValue.heightProperty());
                fieldLivingPlace.textProperty().unbindBidirectional(oldValue.livingPlaceProperty());
                fieldPhoneNumber.textProperty().unbindBidirectional(oldValue.phoneNumberProperty());
                areaDiagnosis.textProperty().unbindBidirectional(oldValue.diagnosisProperty());
            }
            if(newValue == null) {
                fieldPatientName.setText("");
                fieldPatientSurname.setText("");
                fieldDateOfBirth.setValue(LocalDate.now());
                fieldMass.setText("0");
                fieldHeight.setText("0");
                fieldLivingPlace.setText("");
                fieldPhoneNumber.setText("");
                areaDiagnosis.setText("");
            }else {
                fieldPatientName.textProperty().bindBidirectional(newValue.nameProperty());
                fieldPatientSurname.textProperty().bindBidirectional(newValue.surnameProperty());
                fieldDateOfBirth.valueProperty().bindBidirectional(newValue.dateOfBirthProperty());
                fieldMass.textProperty().bindBidirectional(newValue.massProperty(), new NumberStringConverter());
                fieldHeight.textProperty().bindBidirectional(newValue.heightProperty(), new NumberStringConverter());
                fieldLivingPlace.textProperty().bindBidirectional(newValue.livingPlaceProperty());
                fieldPhoneNumber.textProperty().bindBidirectional(newValue.phoneNumberProperty());
                areaDiagnosis.textProperty().bindBidirectional(newValue.diagnosisProperty());
            }
        });

        listPatients.getSelectionModel().selectFirst();

        fieldPatientSurname.textProperty().addListener((observableValue, oldValue, newValue) -> {
            listPatients.getSelectionModel().getSelectedItem().setSurname(newValue);
            dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());
            appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
            tableAppointments.setItems(appointmentsList);
            tableAppointments.refresh();
            listPatients.setItems(patientsList);
            listPatients.refresh();
        });
        fieldPatientName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            listPatients.getSelectionModel().getSelectedItem().setName(newValue);
            dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());
            appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
            tableAppointments.setItems(appointmentsList);
            tableAppointments.refresh();
            listPatients.setItems(patientsList);
            listPatients.refresh();
        });

        fieldDateOfBirth.valueProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        fieldMass.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        fieldHeight.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        fieldLivingPlace.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        fieldPhoneNumber.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        areaDiagnosis.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
    }

    public void deletePatientAction(ActionEvent actionEvent) {
        dao.deletePatient(listPatients.getSelectionModel().getSelectedItem());
        patientsList.removeAll(listPatients.getSelectionModel().getSelectedItem());
        listPatients.refresh();
    }

    public void addPatientAction(ActionEvent actionEvent) {
        patientsList.add(new Patient());
        listPatients.refresh();
        listPatients.getSelectionModel().selectLast();
        dao.addPatient(listPatients.getSelectionModel().getSelectedItem());
    }
    public void addAppointmentAction(ActionEvent actionEvent) {
    }

    public void editAppointmentAction(ActionEvent actionEvent) {
    }

    public void deleteAppointmentAction(ActionEvent actionEvent) {
        dao.deleteAppointment(tableAppointments.getSelectionModel().getSelectedItem());
        appointmentsList.removeAll(tableAppointments.getSelectionModel().getSelectedItem());
        tableAppointments.refresh();
    }

}
