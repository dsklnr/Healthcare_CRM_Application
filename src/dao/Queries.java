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

    /** Insert a new user into the database.
     * @param username The user's username.
     * @param password The user's password.
     * @return Returns a new user.
     * **/
    public static String insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (User_Name, Password)" +
                "VALUES(?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);

        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;
    }

    /** Update an appointment in the database.
     *
     * @param appointmentId The appointment ID.
     * @param title The appointment title.
     * @param description The appointment description.
     * @param location The appointment location.
     * @param type The appointment type.
     * @param startTime The appointment start date & time.
     * @param endTime The appointment end date & time.
     * @param createDate The appointment create date.
     * @param createdBy The appointment created by field.
     * @param updateDateTime The appointment update date & time.
     * @param lastUpdateBy The appointment last updated by field.
     * @param customerId The appointment customer ID.
     * @param userId The appointment user ID.
     * @param contactId The appointment contact ID.
     */
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type,
                                         String startTime, String endTime, String createDate, String createdBy,
                                         String updateDateTime, String lastUpdateBy, int customerId, int userId,
                                         int contactId) throws SQLException {

        String sql = "UPDATE appointments\n" +
                "SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                " Contact_ID = ?\n" +
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

    /** Select the user's user ID.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return Returns the user's user ID.
     */
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

    /** Get all contacts.
     *
     * @return Returns all contacts.
     */
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

    /** Get the contact ID.
     *
     * @param name The contact's name.
     * @return Returns the contact's ID.
     */
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

    /** Get the contact's name.
     *
     * @param contactId The contact ID.
     * @param appointmentId The contact's appointment ID.
     * @return Returns the contact's name.
     */
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

    /** Validates a user login.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return Returns true if the user's username and password match the credentials in the database.
     */
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

    /** Get all appointments.
     *
     * @return Returns all appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "appointments.Create_Date, appointments.Created_By, appointments.Last_Update, " +
                    "appointments.Last_Updated_By, customers.Customer_ID, users.User_ID, contacts.Contact_ID\n" +
                    "FROM appointments, users, customers, contacts\n" +
                    "WHERE customers.Customer_ID = appointments.Customer_ID AND users.User_ID = appointments.User_ID " +
                    "AND contacts.Contact_ID = appointments.Contact_ID\n" +
                    "ORDER BY Start";

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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

    /** Insert an appointment into the database.
     *
     * @param title The appointment title.
     * @param description The appointment description.
     * @param location The appointment location.
     * @param type The appointment type.
     * @param startDateTime The appointment start date & time.
     * @param endDateTime The appointment end date & time.
     * @param createDate The appointment create date & time.
     * @param createdBy The appointment created by field.
     * @param lastUpdate The appointment last update date & time.
     * @param lastUpdatedBy The appointment last updated by field.
     * @param customerId The appointment customer ID.
     * @param userId The appointment user ID.
     * @param contactId The appointment contact ID.
     * @return Returns a new appointment.
     */
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

    /** Get all customers.
     *
     * @return Returns all customers.
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Create_Date, " +
                    "customers.Created_By, customers.Last_Update, customers.Last_Updated_by, " +
                    "first_level_divisions.Division_ID\n" +
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

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");
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

    /** Insert a customer into the database.
     *
     * @param name The customer name.
     * @param address The customer address.
     * @param postalCode The customer postal code.
     * @param phoneNumber The customer phone number.
     * @param createDate The customer create date & time.
     * @param createdBy The customer created by field.
     * @param lastUpdate The customer last update date & time.
     * @param lastUpdateBy The customer last updated by field.
     * @param divisionId The customer division ID.
     */
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

    /** Delete a customer.
     *
     * @param customerId The customer ID.
     * @return Deletes a customer.
     */
    public static int deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** Delete an appointment.
     *
     * @param appointmentId The appointment ID.
     * @return Deletes an appointment.
     */
    public static int deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** Get all countries.
     *
     * @return Returns all countries.
     */
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

    /** Get all divisions.
     *
     * @return Returns all divisions.
     */
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

    /** Get the division ID.
     *
     * @param division The division name.
     * @return Returns the division ID.
     */
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

    /** Get the division name.
     *
     * @param divisionId The division ID.
     * @return Returns the division ID.
     */
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

    /** Get the country ID.
     *
     * @param divisionId The division ID.
     * @return Returns the country ID.
     */
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

    /** Get the country name.
     *
     * @param divisionId The division ID.
     * @param countryId The country ID.
     * @return Returns the country name.
     */
    public static String getCountryName(int divisionId, int countryId) throws SQLException {
        String sql = "SELECT Country FROM countries, first_level_divisions\n" +
                "WHERE first_level_divisions.Division_ID = ? AND first_level_divisions.Country_ID = ? AND " +
                "countries.Country_ID = ?";

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

    /** Get the upcoming month's appointments.
     *
     * @param userId The user ID.
     * @return Returns the users upcoming month of appointments.
     */
    public static ObservableList<Appointment> getNextMonthAppointments(int userId) throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURDATE() AND CURDATE() + " +
                "INTERVAL 1 MONTH";

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

    /** Get the upcoming week's appointments.
     *
     * @param userId The user ID.
     * @return Returns the users upcoming week of appointments.
     */
    public static ObservableList<Appointment> getNextWeekAppointments(int userId) throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE User_ID = ? AND Start BETWEEN CURDATE() AND CURDATE() + " +
                "INTERVAL 7 DAY";

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

    /** Check for an appointment within the next 15 minutes.
     *
     * @param userId The user ID.
     * @return Returns true if the user has an appointment within the next 15 minutes.
     */
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

    /** Get all start dates & times.
     *
     * @return Returns all start dates and times.
     */
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

    /** Get all end dates & times.
     *
     * @return Returns all end dates & times.
     */
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

    /** Get the start date and time for an appointment.
     *
     * @param appointmentId The appointment ID.
     * @return Returns the start date & time for an appointment.
     */
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

    /** Get the end date & time for an appointment.
     *
     * @param appointmentId The appointment ID.
     * @return Returns the end date & time for an appointment.
     */
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

    /** Check if a customer has an appointment.
     *
     * @param customerId The customer ID.
     * @return Returns true if the customer has an appointment.
     */
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

    /** Update the customer in the database.
     *
     * @param customerName The customer name.
     * @param customerAddress The customer address.
     * @param postal The customer postal code.
     * @param customerPhone The customer phone number.
     * @param createDate The customer create date & time.
     * @param createdBy The customer created by field.
     * @param formatUpdateDateTime The customer update date & time.
     * @param lastUpdateBy The customer last update by field.
     * @param divisionId The customer division ID.
     * @param customerId The customer ID.
     * @return Updates the customer in the database.
     */
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
