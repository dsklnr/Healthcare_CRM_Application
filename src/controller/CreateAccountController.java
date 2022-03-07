package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Creating the create account controller. **/
public class CreateAccountController implements Initializable {
    public TextField setUsername;
    public PasswordField setPassword;

    /** Initialize the create account controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();
    }

    /** Insert a new user into the database. **/
    public void onCreateAccount(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        String user = setUsername.getText();
        String password = setPassword.getText();

        String p = String.valueOf(password.strip().chars().peek(encrypt ->{
            char[] userPassword = password.toCharArray();

            ObservableList<String> encryptedPassword = FXCollections.observableArrayList();

            for (char pass : userPassword){
                pass += 10;
                encryptedPassword.add(String.valueOf(pass));
            }
        }));

        String finalPassword = p.chars().toString();

        System.out.println(finalPassword);

        /*
        char[] userPassword = password.toCharArray();

        ObservableList<String> encryptedPassword = FXCollections.observableArrayList();

        for (char pass : userPassword){
            pass += 10;
            encryptedPassword.add(String.valueOf(pass));
        }

         */


        Queries.insertUser(user, p);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        JDBC.closeConnection();

    }
}
