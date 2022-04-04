package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** Creating the queries class **/
public abstract class Queries {

    /** Insert a new user into the database.
     * @param username The user's username.
     * @param password The user's password.
     * @return Returns a new user.
     * **/
    public static String insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users2 (User_Name, Password)" +
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
     * @param patientId The appointment patient ID.
     * @param userId The appointment user ID.
     * @param doctorId The appointment doctor ID.
     */
    public static void updateAppointment(int appointmentId, String title, String description, String location, String type,
                                         String startTime, String endTime, String createDate, String createdBy,
                                         String updateDateTime, String lastUpdateBy, int patientId, int userId,
                                         int doctorId) throws SQLException {

        String sql = "UPDATE appointments\n" +
                "SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Patient_ID = ?, User_ID = ?," +
                " Doctor_ID = ?\n" +
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
        ps.setInt(12, patientId);
        ps.setInt(13, userId);
        ps.setInt(14, doctorId);
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
        String sql = "SELECT User_ID FROM users WHERE User_Name = ? AND Password = ?";

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

    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            int doctorId = rs.getInt("Contact_ID");

            userList.add(new User(userId, username, password, doctorId));
        }
        return userList;
    }

    /** Get all Contact Info.
     *
     * @return Returns all contact info.
     */
    public static ObservableList<ContactInfo> getAllContactInfo() throws SQLException {
        ObservableList<ContactInfo> contactsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM doctors";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int doctorId = rs.getInt("Doctor_ID");
            String name = rs.getString("Doctor_Name");
            String email = rs.getString("Email");

            contactsList.add(new ContactInfo(doctorId, name, email));
        }
        return contactsList;
    }

    /** Get the Doctor ID.
     *
     * @param name The doctor's name.
     * @return Returns the doctor's ID.
     */
    public static int getOnlyDoctorId(String name) throws SQLException {
        String sql = "SELECT Doctor_ID FROM doctors WHERE Doctor_Name = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int doctorId = rs.getInt("Doctor_ID");
            return doctorId;
        }
        return 0;
    }

    public static int getPatientId(String name) throws SQLException{
        String sql = "SELECT Patient_ID\n" +
                "FROM patients\n" +
                "WHERE Patient_Name = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int patientId = rs.getInt("Patient_ID");
            return patientId;
        }
        return 0;
    }

    /** Get the doctor's name.
     *
     * @param doctorId The doctor ID.
     * @param appointmentId The doctor's appointment ID.
     * @return Returns the doctor's name.
     */
    public static String getContactName(int doctorId, int appointmentId) throws SQLException {
        String sql = "SELECT Doctor_Name\n" +
                "FROM appointments, doctors\n" +
                "WHERE doctors.Doctor_ID = ? AND appointments.Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, doctorId);
        ps.setInt(2, appointmentId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String name = rs.getString("Doctor_Name");
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
            int doctorId = rs.getInt("Doctor_ID");
            User user = new User(userId, username, password, doctorId);
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
            String sql = "SELECT Appointment_ID, appointments.Patient_ID, Patient_Name, User_ID, Doctor_Name, Title, Type, Location, Start, End, Description\n" +
                    "FROM appointments, patients, doctors\n" +
                    "WHERE appointments.Patient_ID = patients.Patient_ID AND appointments.Doctor_ID = doctors.Doctor_ID\n" +
                    "ORDER BY Start";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                int patientId = rs.getInt("Patient_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                String patientName = rs.getString("Patient_Name");
                String doctorName = rs.getString("Doctor_Name");
                int userId = rs.getInt("User_ID");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                /*
                ZoneId utcZone = ZoneId.of("UTC");
                ZonedDateTime utcStartTime = start.atZone(utcZone);
                ZonedDateTime localStartTime = utcStartTime.withZoneSameInstant(ZoneId.systemDefault());
                String finalStart = localStartTime.format(dtf);

                ZonedDateTime utcEndTime = end.atZone(utcZone);
                ZonedDateTime localEndTime = utcEndTime.withZoneSameInstant(ZoneId.systemDefault());
                String finalEnd = localEndTime.format(dtf);

                ZonedDateTime utcCreate = createDate.atZone(utcZone);
                ZonedDateTime localCreate = utcCreate.withZoneSameInstant(ZoneId.systemDefault());
                String finalCreate = localCreate.format(dtf);

                ZonedDateTime utcUpdate = lastUpdate.atZone(utcZone);
                ZonedDateTime localUpdate = utcUpdate.withZoneSameInstant(ZoneId.systemDefault());
                String finalUpdate = localUpdate.format(dtf);

                 */

                allAppointmentList.add(new Appointment(appointmentId, title, description, location,
                        type, start.format(dtf), end.format(dtf), patientName, doctorName, "", "",
                        patientId, userId, 0));
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
     * @param patientId The appointment patient ID.
     * @param userId The appointment user ID.
     * @param doctorId The appointment doctor ID.
     * @return Returns a new appointment.
     */
    public static String insertAppointment(String title, String description, String location, String type,
                                           String startDateTime, String endDateTime, String createDate,
                                           String createdBy, String lastUpdate, String lastUpdatedBy,
                                           int patientId, int userId, int doctorId) throws SQLException {

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
        ps.setInt(11, patientId);
        ps.setInt(12, userId);
        ps.setInt(13, doctorId);
        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;

    }

    /** Get all patients.
     *
     * @return Returns all patients.
     */
    public static ObservableList<Patient> getAllPatients() {
        ObservableList<Patient> allPatientList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Patient_ID, Patient_Name, Address, Postal_Code, Phone, countries.Country, Division\n" +
                    "FROM patients, first_level_divisions, countries\n" +
                    "WHERE patients.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID";

            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int patientId = rs.getInt("Patient_ID");
                String name = rs.getString("Patient_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String county = rs.getString("Country");
                String division = rs.getString("Division");

                allPatientList.add(new Patient(patientId, name, address, postalCode, phone, county,
                        division, "", "", 0));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return allPatientList;
    }

    /** Insert a patient into the database.
     *
     * @param name The patient name.
     * @param address The patient address.
     * @param postalCode The patient postal code.
     * @param phoneNumber The patient phone number.
     * @param createDate The patient create date & time.
     * @param createdBy The patient created by field.
     * @param lastUpdate The patient last update date & time.
     * @param lastUpdateBy The patient last updated by field.
     * @param divisionId The patient division ID.
     */
    public static void createPatient(String name, String address, String postalCode, String phoneNumber,
                                     String createDate, String createdBy, String lastUpdate, String lastUpdateBy,
                                     int divisionId) {
        try {
            String sql = "INSERT INTO patients VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    /** Delete a patient.
     *
     * @param patientId The patient ID.
     * @return Deletes a patient.
     */
    public static int deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE Patient_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, patientId);
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
        ObservableList<Country> country = FXCollections.observableArrayList();

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
            int patientIdFK = rs.getInt("Patient_ID");
            int userIdFK = rs.getInt("User_ID");
            int doctorIdFK = rs.getInt("Doctor_ID");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
            String startTime = start.format(dtf);
            String endTime = end.format(dtf);

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                    patientIdFK, userIdFK, doctorIdFK));
        }
        return appointmentsList;
    }

    public static ObservableList<Appointment> getAllNextMonthAppointments() throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Start BETWEEN CURDATE() AND CURDATE() + " +
                "INTERVAL 1 MONTH";

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
            int patientIdFK = rs.getInt("Patient_ID");
            int userIdFK = rs.getInt("User_ID");
            int doctorIdFK = rs.getInt("Doctor_ID");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
            String startTime = start.format(dtf);
            String endTime = end.format(dtf);

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                    patientIdFK, userIdFK, doctorIdFK));
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
            int patientIdFK = rs.getInt("Patient_ID");
            int userIdFK = rs.getInt("User_ID");
            int doctorIdFK = rs.getInt("Doctor_ID");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
            String startTime = start.format(dtf);
            String endTime = end.format(dtf);

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                    patientIdFK, userIdFK, doctorIdFK));
        }
        return appointmentsList;
    }

    public static ObservableList<Appointment> getAllNextWeekAppointments() throws SQLException {
        ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Start BETWEEN CURDATE() AND CURDATE() + " +
                "INTERVAL 7 DAY";

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
            int patientIdFK = rs.getInt("Patient_ID");
            int userIdFK = rs.getInt("User_ID");
            int doctorIdFK = rs.getInt("Doctor_ID");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
            String startTime = start.format(dtf);
            String endTime = end.format(dtf);

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, startTime, endTime, createDate, contact, lastUpdate, updateBy,
                    patientIdFK, userIdFK, doctorIdFK));
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
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String contact = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String updateBy = rs.getString("Last_Updated_By");
            int patientIdFK = rs.getInt("Patient_ID");
            int userIdFK = rs.getInt("User_ID");
            int doctorIdFK = rs.getInt("Doctor_ID");

            appointmentsList.add(new Appointment(appointmentId, title, description, location,
                    type, start.toString(), end.toString(), createDate.toString(), contact, lastUpdate.toString(), updateBy,
                    patientIdFK, userIdFK, doctorIdFK));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime s = start.toLocalDateTime();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointments");
            alert.setContentText("Appointment " + appointmentId + " begins at " + s.format(dtf));
            alert.showAndWait();

            return true;
        }
        return false;
    }

    /** Get all start dates & times.
     *
     * @return Returns all start dates and times.
     */
    public static ObservableList<LocalDateTime> getAllStartTimes(String userId) throws SQLException {
        ObservableList<LocalDateTime> allAppointmentStartTimes = FXCollections.observableArrayList();

        String sql = "SELECT Start FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userId);

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
    public static ObservableList<LocalDateTime> getAllEndTimes(String userId) throws SQLException {
        ObservableList<LocalDateTime> allAppointmentEndTimes = FXCollections.observableArrayList();

        String sql = "SELECT End FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
            allAppointmentEndTimes.add(endDateTime);
        }
        return allAppointmentEndTimes;
    }

    /** Get all other appointment start times.
     *
     * @param appointmentId The appointment ID.
     * @param userId The user ID.
     * @return Returns all other appointment start times.
     */
    public static ObservableList<LocalDateTime> getAllOtherStartTimes(int appointmentId, String userId) throws SQLException{
        ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();

        String sql = "SELECT Start From appointments\n" +
                "WHERE Appointment_ID != ? AND User_ID = ?;";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.setString(2, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
            startTimes.add(startDateTime);
        }
        return startTimes;
    }

    /** Get all other appointment end times.
     *
     * @param appointmentId The appointment ID.
     * @param userId The user ID.
     * @return Returns all other appointment end times.
     */
    public static ObservableList<LocalDateTime> getAllOtherEndTimes(int appointmentId, String userId) throws SQLException{
        ObservableList<LocalDateTime> endTimes = FXCollections.observableArrayList();

        String sql = "SELECT End From appointments\n" +
                "WHERE Appointment_ID != ? AND User_ID = ?;";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.setString(2, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
            endTimes.add(endDateTime);
        }
        return endTimes;
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

    /** Check if a patient has an appointment.
     *
     * @param patientId The patient ID.
     * @return Returns true if the patient has an appointment.
     */
    public static boolean checkDeletePatient(int patientId) throws SQLException {
        String sql = "SELECT * FROM patients, appointments WHERE patients.Patient_ID = ? " +
                "AND appointments.Patient_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, patientId);
        ps.setInt(2, patientId);

        ResultSet rs = ps.executeQuery();

        ObservableList<Patient> patients = FXCollections.observableArrayList();

        while (rs.next()) {
            int id = rs.getInt("Patient_ID");
            String name = rs.getString("Patient_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int division = rs.getInt("Division_ID");

            Patient patient = new Patient(id, name, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, division);
            patients.addAll(patient);
            return false;
        }
        return true;
    }

    /** Update the patient in the database.
     *
     * @param patientName The patient name.
     * @param patientAddress The patient address.
     * @param postal The patient postal code.
     * @param patientPhone The patient phone number.
     * @param createDate The patient create date & time.
     * @param createdBy The patient created by field.
     * @param formatUpdateDateTime The patient update date & time.
     * @param lastUpdateBy The patient last update by field.
     * @param divisionId The patient division ID.
     * @param patientId The patient ID.
     * @return Updates the patient in the database.
     */
    public static String updatePatient(String patientName, String patientAddress, String postal, String patientPhone,
                                       String createDate, String createdBy, String formatUpdateDateTime,
                                       String lastUpdateBy, int divisionId, int patientId) throws SQLException {

        String sql = "UPDATE patients\n" +
                "SET Patient_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Division_ID = ?\n" +
                "WHERE Patient_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, patientName);
        ps.setString(2, patientAddress);
        ps.setString(3, postal);
        ps.setString(4, patientPhone);
        ps.setString(5, createDate);
        ps.setString(6, createdBy);
        ps.setString(7, formatUpdateDateTime);
        ps.setString(8, lastUpdateBy);
        ps.setInt(9, divisionId);
        ps.setInt(10, patientId);

        String rowsAffected = String.valueOf(ps.executeUpdate());
        return rowsAffected;
    }

    /** Get all US states.
     *
     * @return Returns all US States.
     */
    public static ObservableList<Division> getAllUsStates() throws SQLException {
        ObservableList<Division> usStates = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 1";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdateBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdateBy, countryId);
            usStates.add(d);
        }
        return usStates;
    }

    /** Get all countries.
     *
     * @return Returns all countries.
     */
    public static ObservableList<Division> getAllUkCountries() throws SQLException {
        ObservableList<Division> ukCountries = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 2";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdateBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdateBy, countryId);
            ukCountries.add(d);
        }
        return ukCountries;
    }

    /** Get all canadian provinces.
     *
     * @return Returns all canadian provinces.
     */
    public static ObservableList<Division> getAllCanadianProvinces() throws SQLException {
        ObservableList<Division> canadianProvinces = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = 3";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdateBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdateBy, countryId);
            canadianProvinces.add(d);
        }
        return canadianProvinces;
    }

    /** Get all appointment create dates based on appointment ID.
     *
     * @param appointmentId The appointment ID.
     * @return Returns all appointment create dates.
     */
    public static LocalDateTime getAppointmentCreateDate(int appointmentId) throws SQLException {

        String sql = "SELECT Create_Date FROM appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            return rs.getTimestamp("Create_Date").toLocalDateTime();

        }
        return null;
    }

    /** Get the doctor ID based on the user's username.
     *
     * @param username The user's username.
     * @return Returns the user's doctor ID.
     */
    public static int getDoctorId(String username) throws SQLException {
        String sql = "SELECT Doctor_ID\n" +
                "FROM users\n" +
                "WHERE User_Name = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int doctorID = rs.getInt("Doctor_ID");
            return doctorID;
        }
        return 0;
    }
}
