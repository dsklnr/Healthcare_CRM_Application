package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DashboardQueries {

    public static String getDoctorLevel(String username) throws SQLException {
        String sql = "SELECT Doctor_Level\n" +
                "FROM users\n" +
                "WHERE User_Name = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String level = rs.getString("Doctor_Level");
            return level;
        }
        return null;
    }
}
