package main;

import com.sun.javafx.binding.StringFormatter;
import dao.JDBC;
import dao.Queries;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/** Creating the main class. **/

public class Main extends Application {

    /** Set the scene for the main screen. **/
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    /** The main method. This method is empty. **/
    public static void main(String[] args){
        launch(args);
    }
}
