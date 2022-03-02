package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public static void updateAppointment(int appointmentId, String title, String description, String location, String type,
                                         String startTime, String endTime, String createDate, String createdBy,
                                         String updateDateTime, String lastUpdateBy, int customerId, int userId,
                                         int contactId) throws SQLException {

        String sql = "UPDATE appointments\n" +
                "SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?\n" +
                "WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, type);
        ps.setString(6, startTime);
        ps.setString(7, endTime);
        ps.setString(8, createDate);
        ps.setString(9, createdBy);
        ps.setString(10, updateDateTime);
        ps.setString(11, lastUpdateBy);
        ps.setInt(12, customerId);
        ps.setInt(13, userId);
        ps.setInt(14, contactId);
        ps.setInt(15, appointmentId);
        ps.execute();

    }

    public static String getUpdateAppointmentContact(int appointmentId, int contactId, int contactID) throws SQLException {
        String sql = "SELECT Contact_Name\n" +
                "FROM appointments, contacts\n" +
                "WHERE Appointment_ID = ? AND appointments.Contact_ID = ? AND contacts.Contact_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.setInt(2, contactId);
        ps.setInt(3, contactID);

        String rowsAffected = String.valueOf(ps.executeQuery());
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

    public static User selectCurrentUser(String username) throws SQLException {
        ObservableList<User> currentUser = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.getGeneratedKeys();
        //ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String password = rs.getString("Password");
            User current = new User(userId, username, password);
            return current;
        }
        return null;
    }

    public static int selectUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> currentUser = FXCollections.observableArrayList();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            return userId;
        }
        return 0;
    }

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            contactsList.add(new Contact(contactId, name, email));
        }
        return contactsList;
    }

    public static int getContactId(String name) throws SQLException {
        String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            return contactId;
        }
        return 0;
    }

    public static String getContactName(int contactId, int appointmentId) throws SQLException {
        String sql = "SELECT Contact_Name\n" +
                "FROM appointments, contacts\n" +
                "WHERE contacts.Contact_ID = ? AND appointments.Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        ps.setInt(2, appointmentId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String name = rs.getString("Contact_Name");
            return name;
        }
        return null;
    }


    public static boolean login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> userList = FXCollections.observableArrayList();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            User user = new User(userId, username, password);
            userList.add(user);
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

        while (rs.next()) {
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

    public static ObservableList<Appointment> getAllAppointmentsFromId(int userId) {
        ObservableList<Appointment> aList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, appointments.Create_Date, appointments.Created_By, appointments.Last_Update, appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID\n" +
                    "FROM appointments, users, customers, contacts\n" +
                    "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = appointments.User_ID AND contacts.Contact_ID = appointments.Contact_ID AND users.User_ID = ?";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return aList;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, appointments.Create_Date, appointments.Created_By, appointments.Last_Update, appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID\n" +
                    "FROM appointments, users, customers, contacts\n" +
                    "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = appointments.User_ID AND contacts.Contact_ID = appointments.Contact_ID";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                String createDate = rs.getString("Create_Date");
                String contact = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String updateBy = rs.getString("Last_Updated_By");
                int customerIdFK = rs.getInt("Customer_ID");
                int userIdFK = rs.getInt("User_ID");
                int contactIdFK = rs.getInt("Contact_ID");

                ZoneId utcZone = ZoneId.of("UTC");
                ZonedDateTime utcStartTime = start.atZone(utcZone);
                ZonedDateTime localStartTime = utcStartTime.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalLocalStartTime = localStartTime.minusHours(1);

                ZonedDateTime utcEndTime = end.atZone(utcZone);
                ZonedDateTime localEndTime = utcEndTime.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalLocalEndTime = localEndTime.minusHours(1);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
                String startTime = finalLocalStartTime.format(dtf);
                String endTime = finalLocalEndTime.format(dtf);

                allAppointmentList.add(new Appointment(appointmentId, title, description, location,
                        type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                        customerIdFK, userIdFK, contactIdFK));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allAppointmentList;
    }

    public static String insertAppointment(String title, String description, String location, String type,
                                           String startDateTime, String endDateTime, String createDate,
                                           String createdBy, String lastUpdate, String lastUpdatedBy,
                                           int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO appointments VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, startDateTime);
        ps.setString(6, endDateTime);
        ps.setString(7, createDate);
        ps.setString(8, createdBy);
        ps.setString(9, lastUpdate);
        ps.setString(10, lastUpdatedBy);
        ps.setInt(11, customerId);
        ps.setInt(12, userId);
        ps.setInt(13, contactId);
        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;

    }

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Create_Date, customers.Created_By, customers.Last_Update, customers.Last_Updated_by, first_level_divisions.Division_ID\n" +
                    "FROM customers, first_level_divisions\n" +
                    "WHERE customers.Division_ID = first_level_divisions.Division_ID";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_by");
                int divisionId = rs.getInt("Division_ID");

                ZoneId utcZone = ZoneId.of("UTC");
                ZonedDateTime utcCreateDate = createDate.atZone(utcZone);
                ZonedDateTime localCreateDate = utcCreateDate.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalLocalStartDate = localCreateDate.minusHours(1);

                ZonedDateTime utcUpdateDate = lastUpdate.atZone(utcZone);
                ZonedDateTime localUpdateDate = utcUpdateDate.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalLocalUpdateDate = localUpdateDate.minusHours(1);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
                String finalCreateDate = finalLocalStartDate.format(dtf);
                String finalUpdateDate = finalLocalUpdateDate.format(dtf);

                allCustomerList.add(new Customer(customerId, name, address, postalCode, phone, finalCreateDate,
                        createdBy, finalUpdateDate, lastUpdatedBy, divisionId));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allCustomerList;
    }

    public static void createCustomer(String name, String address, String postalCode, String phoneNumber,
                                      String createDate, String createdBy, String lastUpdate, String lastUpdateBy,
                                      int divisionId) {
        try {
            String sql = "INSERT INTO customers VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, createDate);
            ps.setString(6, createdBy);
            ps.setString(7, lastUpdate);
            ps.setString(8, lastUpdateBy);
            ps.setInt(9, divisionId);
            ps.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static int deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    public static int deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static ArrayList<String> getAllCountryNames() {
        ArrayList<String> allCountriesList = new ArrayList<>();

        try {
            String sql = "SELECT Country FROM countries";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String country = rs.getString("Country");
                allCountriesList.add(country);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return allCountriesList;
    }

    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM countries";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country c = new Country(countryId, countryName);
                countryList.add(c);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return countryList;
    }

    public static ObservableList<Division> getAllDivisions() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM first_level_divisions";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                String createDate = rs.getString("Create_Date");
                String createdBy = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int countryId = rs.getInt("Country_ID");
                Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdateBy, countryId);
                divisionList.add(d);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return divisionList;
    }

    public static int getDivisionId(String division) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            return divisionId;
        }
        return 0;

    }

    public static String getDivisionName(int divisionId) throws SQLException {
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String division = rs.getString("Division");
            return division;
        }
        return null;

    }

    public static int getCountryId(int divisionId) throws SQLException {
        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int countryId = rs.getInt("Country_ID");
            return countryId;
        }
        return 0;
    }

    public static String getCountryName(int divisionId, int countryId) throws SQLException {
        String sql = "SELECT Country FROM countries, first_level_divisions\n" +
                "WHERE first_level_divisions.Division_ID = ? AND first_level_divisions.Country_ID = ? AND countries.Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ps.setInt(2, countryId);
        ps.setInt(3, countryId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String countryName = rs.getString("Country");
            return countryName;
        }
        return null;
    }

    public static ObservableList<Appointment> getNextMonthAppointments(int userId) throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURDATE() AND CURDATE() + INTERVAL 1 MONTH";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            String createDate = rs.getString("Create_Date");
            String contact = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int customerIdFK = rs.getInt("Customer_ID");
            int userIdFK = rs.getInt("User_ID");
            int contactIdFK = rs.getInt("Contact_ID");

            ZoneId utcZone = ZoneId.of("UTC");
            ZonedDateTime utcStartTime = start.atZone(utcZone);
            ZonedDateTime localStartTime = utcStartTime.withZoneSameInstant(ZoneOffset.systemDefault());
            ZonedDateTime finalLocalStartTime = localStartTime.minusHours(1);

            ZonedDateTime utcEndTime = end.atZone(utcZone);
            ZonedDateTime localEndTime = utcEndTime.withZoneSameInstant(ZoneOffset.systemDefault());
            ZonedDateTime finalLocalEndTime = localEndTime.minusHours(1);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
            String startTime = finalLocalStartTime.format(dtf);
            String endTime = finalLocalEndTime.format(dtf);

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                    customerIdFK, userIdFK, contactIdFK));
        }
        return appointmentsList;
    }

    public static ObservableList<Appointment> getNextWeekAppointments(int userId) throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURDATE() AND CURDATE() + INTERVAL 7 DAY";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
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

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, start, end, createDate, contact, lastUpdate, updateBy,
                    customerIdFK, userIdFK, contactIdFK));
        }
        return appointmentsList;
    }

    public static boolean immediateAppointment(int userId) throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = " SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURRENT_TIME() AND CURRENT_TIME() + INTERVAL 15 MINUTE";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
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

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, start, end, createDate, contact, lastUpdate, updateBy,
                    customerIdFK, userIdFK, contactIdFK));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointments");
            alert.setContentText("Appointment " + appointmentId + " begins at " + start);
            alert.showAndWait();

            return true;
        }
        return false;
    }

    public static ObservableList<LocalDateTime> getAllStartTimes() throws SQLException {
        ObservableList<LocalDateTime> allAppointmentStartTimes = FXCollections.observableArrayList();

        String sql = "SELECT Start FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
            allAppointmentStartTimes.add(startDateTime);
        }
        return allAppointmentStartTimes;
    }

    public static ObservableList<LocalDateTime> getAllEndTimes() throws SQLException {
        ObservableList<LocalDateTime> allAppointmentEndTimes = FXCollections.observableArrayList();

        String sql = "SELECT End FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
            allAppointmentEndTimes.add(endDateTime);
        }
        return allAppointmentEndTimes;
    }

    public static String getUpdateStartTime(int appointmentId) throws SQLException {
        String sql = "Select Start from appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String startDateTime = rs.getString("Start");
            return startDateTime;
        }
        return null;
    }

    public static String getUpdateEndTime(int appointmentId) throws SQLException {
        String sql = "Select End from appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String endDateTime = rs.getString("End");
            return endDateTime;
        }
        return null;
    }

    public static boolean checkDeleteCustomer(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers, appointments WHERE customers.Customer_ID = ? " +
                "AND appointments.Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.setInt(2, customerId);

        ResultSet rs = ps.executeQuery();

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        while (rs.next()) {
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int division = rs.getInt("Division_ID");

            Customer customer = new Customer(id, name, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, division);
            customers.addAll(customer);
            return false;
        }
        return true;
    }

    public static String updateCustomer(String customerName, String customerAddress, String postal, String customerPhone,
                                        String createDate, String createdBy, String formatUpdateDateTime,
                                        String lastUpdateBy, int divisionId, int customerId) throws SQLException {

        String sql = "UPDATE customers\n" +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ?\n" +
                "WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postal);
        ps.setString(4, customerPhone);
        ps.setString(5, createDate);
        ps.setString(6, createdBy);
        ps.setString(7, formatUpdateDateTime);
        ps.setString(8, lastUpdateBy);
        ps.setInt(9, divisionId);
        ps.setInt(10, customerId);

        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;
    }


}
