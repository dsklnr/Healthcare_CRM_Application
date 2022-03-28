package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class CreateAccountQueries {

    public static void insertDoctor(String userName, String UserName, String password, String level) throws SQLException {
        String sql = "INSERT INTO doctors \n" +
                "VALUES (null, (SELECT User_ID FROM users2 WHERE User_Name = ?), ?, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, UserName);
        ps.setString(3, password);
        ps.setString(4, level);
        ps.execute();
    }


}
