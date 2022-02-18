package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Queries {

    //Create a new user in the SQL database
    public static String insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (User_Name, Password)" +
                "VALUES(?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;
    }

    //Update a user in the SQL database
    public static int updateUser(int userId, String username, String password) throws SQLException {
        String sql = "UPDATE users SET User_Name = ? WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, userId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static void select() throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = ps.getGeneratedKeys();
        //ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userId + " | " + username + ", " + password + "\n");
        }
    }

    public static int selectUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> currentUser = FXCollections.observableArrayList();

        while (rs.next()){
            int userId = rs.getInt("User_ID");
            return userId;
        }
        return 0;
    }

    /*
    public static int selectUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, userId);
        ResultSet rs = ps.getGeneratedKeys();
        //ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userId + " | " + username + ", " + password + "\n");
        }
    }

     */

    public static boolean login(String username, String password) throws SQLException {
        String sql = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> userList = FXCollections.observableArrayList();

        while (rs.next()){
            int userId = rs.getInt("User_ID");
            userList.add(new User(userId, username, password));
            return true;
        }
        return false;

    }

    public static void selectAppointment(int userId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        ObservableList<Appointment> appointment = FXCollections.observableArrayList();

        while (rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Created_By");
            String type = rs.getString("Type");
            String start = rs.getString("Start");
            String end = rs.getString("End");
            int customerIdFK = rs.getInt("Customer_ID");
            int userIdFK = rs.getInt("User_ID");
            //System.out.print(appointmentId + " | " + title + ", " + description
                    //+ ", " + location + ", " + type + ", " + start + ", "
                    //+ end + ", " + customerIdFK + ", " + userIdFK + "\n");
            //appointment.add(new Appointment(appointmentId, title, description, location, contact, type, start, end, customerIdFK, userIdFK));
        }
    }

    public static ObservableList<Appointment> getAllAppointmentsFromId(int userId){
        ObservableList<Appointment> aList = FXCollections.observableArrayList();

        try{
            String sql =
                    "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, appointments.Create_Date, appointments.Created_By, appointments.Last_Update, appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID\n" +
                            "FROM appointments, users, customers, contacts\n" +
                            "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = appointments.User_ID AND contacts.Contact_ID = appointments.Contact_ID AND users.User_ID = ?";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, userId);
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
                String contact = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int customerIdFK = rs.getInt("Customer_ID");
                int userIdFK = rs.getInt("User_ID");
                int contactIdFK = rs.getInt("Contact_ID");

                aList.add(new Appointment(appointmentId, title, description, location,
                        type, start, end, createDate, contact, lastUpdate, updateBy,
                        customerIdFK, userIdFK, contactIdFK));
            }

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return aList;
    }

    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();

        try{
            String sql =
                    "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, appointments.Create_Date, appointments.Created_By, appointments.Last_Update, appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID\n" +
                            "FROM appointments, users, customers, contacts\n" +
                            "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = appointments.User_ID AND contacts.Contact_ID = appointments.Contact_ID";

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
                String contact = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int customerIdFK = rs.getInt("Customer_ID");
                int userIdFK = rs.getInt("User_ID");
                int contactIdFK = rs.getInt("Contact_ID");

                allAppointmentList.add(new Appointment(appointmentId, title, description, location,
                        type, start, end, createDate, contact, lastUpdate, updateBy,
                        customerIdFK, userIdFK, contactIdFK));
            }

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return allAppointmentList;
    }
}