package main;

import helper.JDBC;
import helper.Queries;
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
        JDBC.openConnection();

        /*
        int rowsAffected = Queries.deleteUser(3);

        if (rowsAffected > 0){
            System.out.println("Delete Successful");
        }
        else {
            System.out.println("Insert Unsuccessful");
        }
         */

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
