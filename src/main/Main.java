package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        //Queries.selectAppointment(1);

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
