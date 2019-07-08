package ba.unsa.etf.rs.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PatientDAOBase implements PatientDAO {
    private Connection connection;
    private PreparedStatement getPatientsStatement;
    private PreparedStatement getPatientStatement;
    private PreparedStatement updatePatientStatement;
    private PreparedStatement deletePatientStatement;
    private PreparedStatement getNextPatientIDStatement;
    private PreparedStatement addPatientStatement;
    private PreparedStatement getAppointmentsStatement;
    private PreparedStatement getNextAppointmentIDStatement;
    private PreparedStatement addAppointmentStatement;
    private PreparedStatement deleteAppointmentStatement;
    private static PatientDAOBase instance = null;

    public static PatientDAOBase getInstance() {
        if (instance == null) instance = new PatientDAOBase();
        return instance;
    }


    private PatientDAOBase(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            try {
                getPatientsStatement = connection.prepareStatement("SELECT * FROM patients");
            } catch (SQLException e) {
                regenerateDatabase();
                try {
                    getPatientsStatement = connection.prepareStatement("SELECT * FROM patients");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            updatePatientStatement = connection.prepareStatement("UPDATE patients SET name=?, surname=?,phone_number=?," +
                    "living_place=?, diagnosis=?, date_of_birth=?, mass=?, height=? WHERE  id=?");

            deletePatientStatement = connection.prepareStatement("DELETE FROM patients WHERE id=?");
            getNextPatientIDStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM patients");
            addPatientStatement = connection.prepareStatement("INSERT INTO patients VALUES (?,?,?,?,?,?,?,?,?)");
            getPatientStatement = connection.prepareStatement("SELECT * FROM patients WHERE id=?");
            getAppointmentsStatement = connection.prepareStatement("SELECT * FROM appointments");
            getNextAppointmentIDStatement = connection.prepareStatement("SELECT MAX(id)+1 FROM appointments");
            addAppointmentStatement = connection.prepareStatement("INSERT INTO appointments VALUES (?,?,?)");
            deleteAppointmentStatement = connection.prepareStatement("DELETE FROM appointments WHERE id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Patient getPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)
        ,rs.getString(6), rs.getDouble(8), rs.getDouble(9), rs.getDate(7).toLocalDate());
        return patient;
    }


    public ArrayList<Patient> getPatients(){
        ArrayList<Patient> result = new ArrayList();
        try {
            ResultSet rs = getPatientsStatement.executeQuery();
            while (rs.next()) {
                Patient grad = getPatientFromResultSet(rs);
                result.add(grad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void updatePatient(Patient patient) {
        try {
            updatePatientStatement.setString(1, patient.getName());
            updatePatientStatement.setString(2,patient.getSurname());
            updatePatientStatement.setString(3,patient.getPhoneNumber());
            updatePatientStatement.setString(4,patient.getLivingPlace());
            updatePatientStatement.setString(5,patient.getDiagnosis());
            updatePatientStatement.setDate(6, Date.valueOf(patient.getDateOfBirth()));
            updatePatientStatement.setDouble(7, patient.getMass());
            updatePatientStatement.setDouble(8, patient.getHeight());
            updatePatientStatement.setInt(9, patient.getId());
            updatePatientStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deletePatient(Patient patient) {
        try {
            deletePatientStatement.setInt(1, patient.getId());
            deletePatientStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addPatient(Patient patient) {
        try {
            ResultSet rs = getNextPatientIDStatement.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            addPatientStatement.setInt(1, id);
            addPatientStatement.setString(2, patient.getName());
            addPatientStatement.setString(3,patient.getSurname());
            addPatientStatement.setString(4,patient.getPhoneNumber());
            addPatientStatement.setString(5,patient.getLivingPlace());
            addPatientStatement.setString(6,patient.getDiagnosis());
            addPatientStatement.setDate(7,Date.valueOf(patient.getDateOfBirth()));
            addPatientStatement.setDouble(8, patient.getMass());
            addPatientStatement.setDouble(9,patient.getHeight());
            addPatientStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void regenerateDatabase() {
        Scanner input = null;
        try {
            input = new Scanner(new FileInputStream("database.db.sql"));
            String sqlStatement = "";
            while (input.hasNext()) {
                sqlStatement += input.nextLine();
                if ( sqlStatement.charAt( sqlStatement.length()-1 ) == ';') {
                    System.out.println("Executing statement: "+sqlStatement);
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(sqlStatement);
                        sqlStatement = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public int getNextPatientID(){
        ResultSet rs = null;
        int id = 1;
        try {
            rs = getNextPatientIDStatement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    private Patient getPatient(int id) {
        try {
            getPatientStatement.setInt(1, id);
            ResultSet rs = getPatientStatement.executeQuery();
            if (!rs.next()) return null;
            return getPatientFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public ArrayList<Appointment> getAppointments() {
        ArrayList<Appointment> result = new ArrayList();
        try {
            ResultSet rs = getAppointmentsStatement.executeQuery();
            while (rs.next()) {
                Patient patient = getPatient(rs.getInt(3));
                Appointment appointment = getAppointmentFromResulstSet(rs, patient);
                result.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Appointment getAppointmentFromResulstSet(ResultSet rs, Patient patient) throws SQLException {
        return new Appointment(rs.getInt(1), rs.getTimestamp(2), patient);
    }


    public void deleteAppointment(Appointment appointment) {
        try {
            deleteAppointmentStatement.setInt(1, appointment.getId());
            deleteAppointmentStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
