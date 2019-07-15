package ba.unsa.etf.rs.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Optional;

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
        tableAppointments.getSelectionModel().selectFirst();

        listPatients.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue != null) {
                fieldPatientName.textProperty().unbindBidirectional(oldValue.nameProperty());
                fieldPatientSurname.textProperty().unbindBidirectional(oldValue.surnameProperty());
                fieldDateOfBirth.valueProperty().unbindBidirectional(oldValue.dateOfBirthProperty());
                fieldMass.textProperty().unbindBidirectional(oldValue.massProperty());
                fieldHeight.textProperty().unbindBidirectional(oldValue.heightProperty());
                fieldLivingPlace.textProperty().unbindBidirectional(oldValue.livingPlaceProperty());
                fieldPhoneNumber.textProperty().unbindBidirectional(oldValue.phoneNumberProperty());
                areaDiagnosis.textProperty().unbindBidirectional(oldValue.diagnosisProperty());
            }
            if (newValue == null) {
                fieldPatientName.setText("");
                fieldPatientSurname.setText("");
                fieldDateOfBirth.setValue(LocalDate.now());
                fieldMass.setText("0");
                fieldHeight.setText("0");
                fieldLivingPlace.setText("");
                fieldPhoneNumber.setText("");
                areaDiagnosis.setText("");
            } else {
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
            validateFields();
            listPatients.getSelectionModel().getSelectedItem().setSurname(newValue);
            dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());
            appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
            tableAppointments.setItems(appointmentsList);
            tableAppointments.refresh();
            listPatients.setItems(patientsList);
            listPatients.refresh();
        });
        fieldPatientName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            validateFields();
            if(patientsList.isEmpty()) return;
            listPatients.getSelectionModel().getSelectedItem().setName(newValue);
            dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());
            appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
            tableAppointments.setItems(appointmentsList);
            tableAppointments.refresh();
            listPatients.setItems(patientsList);
            listPatients.refresh();
        });

        fieldDateOfBirth.valueProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
        fieldMass.textProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
        fieldHeight.textProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
        fieldLivingPlace.textProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
        fieldPhoneNumber.textProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
        areaDiagnosis.textProperty().addListener((observableValue, s, t1) -> {validateFields();if(patientsList.isEmpty()) return; dao.updatePatient(listPatients.getSelectionModel().getSelectedItem());});
    }

    public void deletePatientAction(ActionEvent actionEvent) {
        if (listPatients.getSelectionModel().isEmpty()) return;
        Patient patient = listPatients.getSelectionModel().getSelectedItem();
        if (patient == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Deleting patient " + patient.toString());
        alert.setContentText("Are you sure you want to delete patient " +patient.toString()+" and all related appointments?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Delete all related appointments first
            dao.deleteAppointmentByPatient(listPatients.getSelectionModel().getSelectedItem());
            appointmentsList = FXCollections.observableArrayList(dao.getAppointments());
            tableAppointments.setItems(appointmentsList);
            tableAppointments.refresh();
            //Delete patient
            dao.deletePatient(listPatients.getSelectionModel().getSelectedItem());
            patientsList.removeAll(listPatients.getSelectionModel().getSelectedItem());
            listPatients.refresh();
        }
    }

    public void addPatientAction() {
        patientsList.add(new Patient());
        listPatients.refresh();
        listPatients.getSelectionModel().selectLast();
        dao.addPatient(listPatients.getSelectionModel().getSelectedItem());
    }

    public void addAppointmentAction() {
        if (patientsList.isEmpty()) return;
        Stage stage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"));
            AppointmentController appointmentController = new AppointmentController(null, dao.getPatients());
            loader.setController(appointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, 600, 250));
            stage.setMinWidth(600);
            stage.setMaxWidth(600);
            stage.setMinHeight(250);
            stage.setMaxHeight(250);
            stage.show();
            stage.setOnHiding(event -> {
                Appointment appointment = appointmentController.getAppointment();
                if (appointment != null) {
                    dao.addAppointment(appointment);
                    appointmentsList.setAll(dao.getAppointments());
                    tableAppointments.refresh();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAppointmentAction() {
        if (tableAppointments.getSelectionModel().isEmpty()) return;
        Stage stage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/appointment.fxml"));
            AppointmentController appointmentController = new AppointmentController(tableAppointments.getSelectionModel().getSelectedItem(), dao.getPatients());
            loader.setController(appointmentController);
            root = loader.load();
            stage.setTitle("Appointment");
            stage.setMinWidth(600);
            stage.setMaxWidth(600);
            stage.setMinHeight(250);
            stage.setMaxHeight(250);
            stage.setScene(new Scene(root, 600, 250));
            stage.show();

            stage.setOnHiding(event -> {
                Appointment appointment = appointmentController.getAppointment();
                if (appointment != null) {
                    dao.updateAppointment(appointment);
                    appointmentsList.setAll(dao.getAppointments());
                    tableAppointments.refresh();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointmentAction() {
        if (tableAppointments.getSelectionModel().isEmpty()) return;
        Appointment appointment = tableAppointments.getSelectionModel().getSelectedItem();
        if (appointment == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion");
        alert.setHeaderText("Deleting appointment for " + appointment.getPatient().toString());
        alert.setContentText("Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            dao.deleteAppointment(tableAppointments.getSelectionModel().getSelectedItem());
            appointmentsList.removeAll(tableAppointments.getSelectionModel().getSelectedItem());
            tableAppointments.refresh();
        }

    }

    public void validateFields(){
        if(fieldPatientName.getText().trim().isEmpty()) {
            fieldPatientName.getStyleClass().add("invalid");
        } else {
            fieldPatientName.getStyleClass().removeAll("invalid");
        }
        if(fieldPatientSurname.getText().trim().isEmpty()) {
            fieldPatientSurname.getStyleClass().add("invalid");
        } else {
            fieldPatientSurname.getStyleClass().removeAll("invalid");
        }
        if(fieldLivingPlace.getText().trim().isEmpty()) {
            fieldLivingPlace.getStyleClass().add("invalid");
        } else {
            fieldLivingPlace.getStyleClass().removeAll("invalid");
        }
        if(fieldPhoneNumber.getText().trim().isEmpty()) {
            fieldPhoneNumber.getStyleClass().add("invalid");
        } else {
            fieldPhoneNumber.getStyleClass().removeAll("invalid");
        }
        if(fieldHeight.getText().trim().isEmpty()) {
            fieldHeight.getStyleClass().add("invalid");
        } else {
            fieldHeight.getStyleClass().removeAll("invalid");
        }
        if(fieldMass.getText().trim().isEmpty()) {
            fieldMass.getStyleClass().add("invalid");
        } else {
            fieldMass.getStyleClass().removeAll("invalid");
        }
    }


    public void generateReportAction() {
        PrintWriter output = null;
        File file = new File(System.getProperty("user.home") + "/Desktop/appointments.txt");
        try {
            FileWriter writer = new FileWriter(file, false);
            output = new PrintWriter(writer);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Report not generated");
            alert.setHeaderText("Report could not be generated at " + System.getProperty("user.home") + "/Desktop/appointments.txt");
            alert.showAndWait();
            e.printStackTrace();
        }
        if(output != null) {
            output.println(String.format("%3s", "#") + " " + String.format("%-20s" , "Patient") + " " +
                    String.format("%-8s", "Date") + " " + String.format("%-5s", "Time"));
            output.println("---------------------------------------");
            for (Appointment appointment : dao.getAppointments()) {
                output.println(String.format("%3d",appointment.getId()) + " " + String.format("%-20s" , appointment.getPatient().toString()) + " " +
                        String.format("%tD", appointment.getAppointmentDate()) + " " + String.format("%tR", appointment.getAppointmentTime()));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report generated");
            alert.setHeaderText("Report generated at " + System.getProperty("user.home") + "/Desktop/appointments.txt");
            alert.showAndWait();
            output.close();
        }
    }
}
