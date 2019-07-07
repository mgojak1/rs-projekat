package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;

public class Controller {
    public TableView tableAppointments;
    public TableColumn colAppointmentNumber;
    public TableColumn colAppointmentPatient;
    public TableColumn colAppointmentDate;
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

    public Controller() {
        dao = PatientDAOBase.getInstance();
        patientsList = FXCollections.observableArrayList(dao.getPatients());
    }

    @FXML
    public void initialize() {
        listPatients.setItems(patientsList);
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

        fieldPatientName.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
        fieldPatientSurname.textProperty().addListener((observableValue, s, t1) -> dao.updatePatient(listPatients.getSelectionModel().getSelectedItem()));
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
//        listPatients.getSelectionModel().selectLast();
    }

}
