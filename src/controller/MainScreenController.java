package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public Button login;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialized");
    }

    public void onLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }
}
