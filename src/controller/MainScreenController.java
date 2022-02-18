package controller;

import dao.JDBC;
import dao.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public AnchorPane anchorPane;
    public TextField username;
    public PasswordField password;
    public Button login;
    public Label crmLabel;
    public Label usernameLabel;
    public Label passwordLabel;
    public Label createLabel;
    public Label forgotLabel;
    public Label time;
    public Label locationLabel;

    //Initialize the main screen
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm   " +
                "MM-dd-yyyy");
        String formatDateTime = ldt.format(format);
        time.setText(formatDateTime);

        crmLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(crmLabel, 0.0);
        AnchorPane.setRightAnchor(crmLabel, 0.0);
        crmLabel.setAlignment(Pos.CENTER);

        createLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(createLabel, 0.0);
        AnchorPane.setRightAnchor(createLabel, 0.0);
        createLabel.setAlignment(Pos.CENTER);

        forgotLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(forgotLabel, 0.0);
        AnchorPane.setRightAnchor(forgotLabel, 0.0);
        forgotLabel.setAlignment(Pos.CENTER);

        locationLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(locationLabel, 0.0);
        AnchorPane.setRightAnchor(locationLabel, 0.0);
        locationLabel.setAlignment(Pos.CENTER);

        locationLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(locationLabel, 0.0);
        AnchorPane.setRightAnchor(locationLabel, 0.0);
        locationLabel.setAlignment(Pos.CENTER);

        String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        locationLabel.setText(country);

        if (Locale.getDefault().getLanguage().equals("fr")) {
            try {
                ResourceBundle rb = ResourceBundle.getBundle("properties/Language", Locale.getDefault());

                if (Locale.getDefault().getLanguage().equals("fr"))
                    crmLabel.setText(rb.getString("crmLogin"));
                usernameLabel.setText(rb.getString("username"));
                passwordLabel.setText(rb.getString("password"));
                createLabel.setText(rb.getString("createAccount"));
                forgotLabel.setText(rb.getString("forgotPassword"));
                login.setText(rb.getString("login"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Handle login events
    public void onLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String user = username.getText();
        String pass = password.getText();

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        String formatDateTime1 = ldt.format(format);

        String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();

        if (Queries.login(user, pass)) {

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                bw.write("Username: " + user + " " + formatDateTime1 + " " + country + " SUCCESSFUL LOGIN \n");
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            User currentUser = new User(Queries.selectUser(user, pass), user, pass);

            currentUser.setUserId(Queries.selectUser(user, pass));
            currentUser.setUsername(user);
            currentUser.setPassword(pass);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
            Parent root = loader.load();

            DashboardController dashboardUser = loader.getController();
            dashboardUser.initialize(currentUser);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            stage.setTitle("CRM Dashboard");
            stage.setScene(new Scene(root, 1500, 800));
            stage.show();
            JDBC.closeConnection();
        }

        else if (!Queries.login(user, pass)){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                    bw.write("Username: " + user + " " + formatDateTime1 + " " + country + " UNSUCCESSFUL LOGIN \n");
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Informations D'Identification Incorrectes");
                alert.setContentText("Nom d'utilisateur ou mot de passe invalide");
                alert.show();
            }

            else{
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                    bw.write("Username: " + user + " " + formatDateTime1 + " " + country + " UNSUCCESSFUL LOGIN \n");
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect Credentials");
                alert.setContentText("Invalid username or password");
                alert.show();

            }
        }
    }

    //Create a new account
    public void onCreateAccount(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateAccount.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        JDBC.closeConnection();
    }

    //Recover lost password
    public void onForgotPassword(MouseEvent mouseEvent) {

    }
}
