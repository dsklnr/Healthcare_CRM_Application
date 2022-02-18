package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAppointment {

    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> aList = FXCollections.observableArrayList();
        //JDBC.openConnection();

        try{
            String sql = "USE client_schedule;" +
                    "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "appointments.Create_Date, appointments.Created_By, appointments.Last_Update, " +
                    "appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID" +
                    "FROM appointments, users, customers, contacts" +
                    "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = " +
                    "appointments.User_ID AND contacts.Contact_ID = appointments.Contact_ID";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String start = rs.getString("Start");
                String end = rs.getString("End");
                String createDate = rs.getString("Create_Date");
                String lastUpdate = rs.getString("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                String contact = rs.getString("Created_By");
                int customerIdFK = rs.getInt("Customer_ID");
                int userIdFK = rs.getInt("User_ID");
                int contactIdFK = rs.getInt("Contact_ID");

                Appointment appointment = new Appointment(appointmentId, title, description, location,
                        type, start, end, createDate, lastUpdate, updateBy, contact, customerIdFK, userIdFK, contactIdFK);
                aList.add(appointment);
            }

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return aList;
    }
}
