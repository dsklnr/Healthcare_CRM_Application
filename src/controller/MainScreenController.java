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
import model.User;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.ResourceBundle;

/** Creating the main/login screen controller. **/
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

    /** Initialize the dashboard controller. **/
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

        ZoneId system = ZoneId.systemDefault();
        locationLabel.setText(String.valueOf(system));

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

    /** Upon selecting login, validate user credentials and the computers default language. **/
    public void onLogin(ActionEvent actionEvent) throws IOException, SQLException {
        JDBC.openConnection();

        String user = username.getText();
        String pass = password.getText();

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        String formatDateTime1 = ldt.format(format);

        String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        String zone = String.valueOf(ZoneId.systemDefault());

        if (Queries.login(user, pass)) {

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                bw.write("Username: " + user + " | " + formatDateTime1 + " " + country + " " + zone + " | " +
                        "SUCCESSFUL LOGIN \n");
                bw.close();

                Queries.immediateAppointment(Queries.selectUser(user, pass));

                if (!Queries.immediateAppointment(Queries.selectUser(user, pass))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointments");
                    alert.setContentText("No immediate upcoming appointments.");
                    alert.showAndWait();

                }

                User currentUser = new User(Queries.selectUser(user, pass), user, pass);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
                Parent root = loader.load();

                DashboardController dashboardController = loader.getController();
                dashboardController.setUser(currentUser);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
                stage.setTitle("CRM Dashboard");
                stage.setScene(new Scene(root, 1500, 800));
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        else if (!Queries.login(user, pass)){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                    bw.write("Username: " + user + " | " + formatDateTime1 + " " + country + " " + zone + " | " +
                            "UNSUCCESSFUL LOGIN\n");
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
                    bw.write("Username: " + user + " | " + formatDateTime1 + " " + country + " " + zone + " | " +
                            "UNSUCCESSFUL LOGIN\n");
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
        JDBC.closeConnection();
    }

    /** Upon selecting create account, display the create account screen. **/
    public void onCreateAccount(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CreateAccount.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        JDBC.closeConnection();
    }

    /** Upon selecting forgot password, display the forgot password screen. **/
    public void onForgotPassword(MouseEvent mouseEvent) {

    }

}
