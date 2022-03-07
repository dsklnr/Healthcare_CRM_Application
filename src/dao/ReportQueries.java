package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Creating the report queries class **/
public abstract class ReportQueries {

    /** Get all the customer appointments.
     * @return Returns all customer appointments.
     */
    public static ObservableList<Appointment> getTotalCustomerAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT Type, MONTHNAME(Start) AS Month, COUNT(Type) AS Count_Type\n" +
                "FROM appointments\n" +
                "GROUP by Start";

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

    /** Get the number of customers by state.
     *
     * @return Returns the number of customers by state.
     */
    public static ObservableList<Division> getNumberOfCustomersByState() throws SQLException {
        ObservableList<Division> allCustomers = FXCollections.observableArrayList();

        String sql = "SELECT countries.Country, first_level_divisions.Division, COUNT(distinct customers.Customer_ID) AS Num_Of_Customers\n" +
                "FROM ((first_level_divisions\n" +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID)\n" +
                "INNER JOIN customers ON first_level_divisions.Division_ID = customers.Division_ID)\n" +
                "GROUP BY countries.Country\n" +
                "order by countries.Country";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String country = rs.getString("Country");
            String division = rs.getString("Division");
            int customers = rs.getInt("Num_Of_Customers");

            allCustomers.add(new Division(1, country, division, "", "", "", customers));

        }


        return allCustomers;
    }

    /** Get a schedule for all contacts.
     *
     * @return Returns a schedule for all contacts.
     */
    public static ObservableList<Appointment> getContactSchedule() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String sql = "SELECT Customer_ID, Appointment_ID, Title, Type, Description, Start, End\n" +
                "FROM appointments\n" +
                "ORDER BY Customer_ID";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");

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

            allAppointments.add(new Appointment(appointmentId, title, description, "", type, startTime, endTime,
                    "", "", "", "", customerId, 1, 1));

        }

        return allAppointments;
    }
}
