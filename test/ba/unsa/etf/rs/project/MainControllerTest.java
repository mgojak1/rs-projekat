package ba.unsa.etf.rs.project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class MainControllerTest {
    Stage theStage;
    Controller controller;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        controller = new Controller();
        controller.resetDao();
        loader.setController(controller);
        Parent root = loader.load();
        stage.setTitle("Doctor's office");
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
        theStage = stage;
    }

    @Test
    public void testTableView(FxRobot robot) {
        controller.resetDao();
        TableView tableAppointments = robot.lookup("#tableAppointments").queryAs(TableView.class);
        assertEquals(3, tableAppointments.getItems().size());
    }

    @Test
    public void testAddAppointment(FxRobot robot) {
        controller.resetDao();
        PatientDAOBase dao = PatientDAOBase.getInstance();
        robot.clickOn("#btnAddAppointment");
        robot.clickOn("#btnOK");

        ArrayList<Appointment> appointments = dao.getAppointments();
        assertEquals(3, appointments.size());
    }

    @Test
    public void testAddPatient(FxRobot robot) {
        controller.resetDao();
        PatientDAOBase dao = PatientDAOBase.getInstance();
        robot.clickOn("#tabPatients");
        robot.clickOn("#btnAddPatient");
        robot.clickOn("#fieldPatientName");
        robot.write("Test");
        robot.clickOn("#fieldPatientSurname");
        robot.write("Testy");
        robot.clickOn("#fieldHeight");
        robot.eraseText(1);
        robot.write("171.2");
        robot.clickOn("#fieldMass");
        robot.eraseText(1);
        robot.write("54.3");
        robot.clickOn("#fieldLivingPlace");
        robot.write("Arizona");
        robot.clickOn("#fieldPhoneNumber");
        robot.write("061/111-123");
        robot.clickOn("#areaDiagnosis");
        robot.write("Diagnosis");

        ArrayList<Patient> patients = dao.getPatients();
        assertEquals("Test", patients.get(3).getName());
        assertEquals("Testy", patients.get(3).getSurname());
    }
}
