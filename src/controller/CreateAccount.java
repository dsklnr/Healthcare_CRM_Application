package controller;

import dao.JDBC;
import dao.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAccount implements Initializable {
    public TextField setUsername;
    public PasswordField setPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();
    }

    public void onCreateAccount(ActionEvent actionEvent) throws SQLException, IOException {
        String user = setUsername.getText();
        String password = setPassword.getText();

        Queries.insertUser(user, password);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
        JDBC.closeConnection();

    }
}
