package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Queries {

    public static int insertUser(int userId, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (User_ID, User_Name, Password)" +
                "VALUES(?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, username);
        ps.setString(3, password);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

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
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userId + " | " + username + ", " + password + "\n");
        }
    }

    public static void selectAppointment(int userId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            int userIdFK = rs.getInt("User_ID");
            System.out.print(appointmentId + " | " + title + ", " + description
                    + ", " + userIdFK + "\n");

        }
    }
}
