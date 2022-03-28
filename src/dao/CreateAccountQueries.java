package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CreateAccountQueries {

    public static void insertDoctor(String userName, String password, String level) throws SQLException {
        String sql = "INSERT INTO users \n" +
                "VALUES (null, ?, ?, ?, null, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ps.setString(3, level);
        ps.execute();
    }

    public static void insertNurse(String userName, String password, String type) throws SQLException {
        String sql = "INSERT INTO users \n" +
                "VALUES (null, ?, ?, null, ?, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        ps.setString(3, type);
        ps.execute();
    }

    public static void insertContact(String name, String email) throws SQLException {
        String sql = "INSERT INTO contacts \n" +
                "VALUES (null, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.execute();
    }

    public static int getId(String username) throws SQLException {
        String sql = "SELECT User_ID\n" +
                "FROM users\n" +
                "WHERE User_Name = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int id = rs.getInt("User_ID");
            return id;
        }

        return 0;
    }


}
