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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Doctor;
import model.User;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/** Creating the main class. **/

public class Main extends Application {
//

    /** Set the scene for the main screen. **/
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 400, 600));
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.show();
    }

    /** The main method. This method is empty. **/
    public static void main(String[] args){
        launch(args);
    }
}
