package controller;

import helper.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public Button login;
    public TextField username;
    public TextField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String user = username.getText();
        String pass = password.getText();


        if (Queries.login(user, pass) == true) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            stage.setTitle("CRM Dashboard");
            stage.setScene(new Scene(root, 1500, 800));
            stage.show();
        }
        else {
            //TODO Error Handling
        }
    }

    public void onCreateAccount(MouseEvent mouseEvent) {

    }

    public void onForgotPassword(MouseEvent mouseEvent) {

    }
}
