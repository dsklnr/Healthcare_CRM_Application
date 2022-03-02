package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ReportQueries {

    public static ObservableList<Appointment> getTotalCustomerAppointments() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String sql = "SELECT Type, EXTRACT(MONTH FROM Start) AS Month, COUNT(Type) AS Count_Type\n" +
                "FROM appointments\n" +
                "GROUP by Start";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String type = rs.getString("Type");
            String month = rs.getString("Month");
            String number = rs.getString("Count_Type");

            appointments.add(new Appointment(1, "", "", "",
                    type, month, "", number, "", "", "",
                    1, 1, 1));
        }

        return appointments;

    }

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
}
