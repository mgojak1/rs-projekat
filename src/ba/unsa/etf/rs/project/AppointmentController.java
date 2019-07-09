package ba.unsa.etf.rs.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentController {
    public TextField fieldHours;
    public TextField fieldMinutes;
    public Slider sliderHours;
    public Slider sliderMinutes;
    public ChoiceBox<Patient> choicePatients;
    public DatePicker fieldAppointmentDate;
    private Appointment appointment;
    private ObservableList<Patient> patients;

    public AppointmentController(Appointment appointment, ArrayList<Patient> patients) {
        this.appointment = appointment;
        this.patients = FXCollections.observableArrayList(patients);

    }

    public Appointment getAppointment() {return appointment;}

    private int findId() {
        int id = 0;
        for (Patient p: patients
             ) {
            if(p.getId() == appointment.getPatient().getId()) id = patients.indexOf(p);
        }

        return id;
    }

    //TODO add files
    //TODO add css & validation
    //TODO add Alerts


    @FXML
    public void initialize() {

        choicePatients.setItems(patients);

        if(appointment == null) {
            choicePatients.getSelectionModel().selectFirst();
            fieldAppointmentDate.setValue(LocalDate.now());
        } else {
            choicePatients.getSelectionModel().select(findId());
            sliderMinutes.valueProperty().setValue(appointment.getAppointmentTime().getMinutes());
            sliderHours.valueProperty().setValue(appointment.getAppointmentTime().getHours());
            fieldAppointmentDate.valueProperty().setValue(appointment.getAppointmentDate());
        }



        fieldHours.setText(String.valueOf(sliderHours.valueProperty().intValue()));
        fieldMinutes.setText(String.valueOf(sliderMinutes.valueProperty().intValue()));
        sliderHours.valueProperty().addListener((observableValue, oldValue, newValue) -> fieldHours.setText(String.valueOf(newValue.intValue())));
        sliderMinutes.valueProperty().addListener((observableValue, oldValue, newValue) -> fieldMinutes.setText(String.valueOf(newValue.intValue())));


    }

    public void okAction() {

        if(appointment == null) {
            appointment = new Appointment();
        }
        appointment.setPatient(choicePatients.getValue());
        appointment.setAppointmentDate(fieldAppointmentDate.getValue());
        appointment.setAppointmentTime(Time.valueOf(fieldHours.getText() + ":" + fieldMinutes.getText() + ":00"));
        Stage stage = (Stage) fieldHours.getScene().getWindow();
        stage.close();

    }

    public void cancelAction() {
        appointment = null;
        Stage stage = (Stage) fieldHours.getScene().getWindow();
        stage.close();
    }




}
