package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Creating the report queries class **/
public abstract class ReportQueries {

    /** Get all the patient appointments.
     * @return Returns all patient appointments.
     */
    public static ObservableList<Appointment> getTotalPatientAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT distinct Type, MONTHNAME(Start) AS Month, COUNT(Type) AS Count_Type\n" +
                "FROM appointments\n" +
                "GROUP by Type";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String type = rs.getString("Type");
            String month = rs.getString("Month");
            String number = rs.getString("Count_Type");

            allAppointments.add(new Appointment(1, "", "", "",
                    type, month, "", number, "", "", "",
                    1, 1, 1));
        }

        return allAppointments;

    }

    /** Get the number of patients by state.
     *
     * @return Returns the number of patients by state.
     */
    public static ObservableList<Division> getNumberOfPatientsByState() throws SQLException {
        ObservableList<Division> allPatients = FXCollections.observableArrayList();

        String sql = "SELECT countries.Country, first_level_divisions.Division, COUNT(distinct patients.Patient_ID) AS Num_Of_Patients\n" +
                "FROM ((first_level_divisions\n" +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID)\n" +
                "INNER JOIN patients ON first_level_divisions.Division_ID = patients.Division_ID)\n" +
                "GROUP BY countries.Country\n" +
                "order by countries.Country";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String country = rs.getString("Country");
            String division = rs.getString("Division");
            int patients = rs.getInt("Num_Of_Patients");

            allPatients.add(new Division(1, country, division, "", "", "", patients));

        }


        return allPatients;
    }
}
