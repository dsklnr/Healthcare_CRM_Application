package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ReportQueries {

    public static ObservableList<String> getTotalCustomerAppointments() throws SQLException {
        ObservableList<String> appointments = FXCollections.observableArrayList();

        String sql = "SELECT Type, EXTRACT(MONTH FROM Start), COUNT(Type)\n" +
                "FROM appointments\n" +
                "group by Type";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String type = rs.getString("Type");
            String month = rs.getString("EXTRACT(MONTH FROM Start)");
            String number = rs.getString("COUNT(Type)");

            appointments.addAll(type, month, number);
        }

        return appointments;

    }

    public static ObservableList<String> getCustomersByState(){
        ObservableList<String> customers = FXCollections.observableArrayList();

        String sql = "SELECT countries.Country, first_level_divisions.Division, COUNT(distinct customers.Customer_ID)\n" +
                "FROM ((first_level_divisions\n" +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID)\n" +
                "INNER JOIN customers ON first_level_divisions.Division_ID = customers.Division_ID)\n" +
                "GROUP BY countries.Country\n" +
                "order by countries.Country";

        return customers;
    }
}
