package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public Label time;
    public Label locationLabel;
    public ImageView logo;

    /** Initialize the dashboard controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/BB_Logo.jpeg");
        logo.setImage(image);

        crmLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1C6CF6;");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1C6CF6;");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1C6CF6;");
        createLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1C6CF6;");

        crmLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(crmLabel, 0.0);
        AnchorPane.setRightAnchor(crmLabel, 0.0);
        crmLabel.setAlignment(Pos.CENTER);

        createLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(createLabel, 0.0);
        AnchorPane.setRightAnchor(createLabel, 0.0);
        createLabel.setAlignment(Pos.CENTER);

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

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm   " +
                "MM-dd-yyyy");
        String formatDateTime = ldt.format(format);
        time.setText(formatDateTime);

        if (Locale.getDefault().getLanguage().equals("fr")) {
            try {
                ResourceBundle rb = ResourceBundle.getBundle("properties/Language", Locale.getDefault());

                if (Locale.getDefault().getLanguage().equals("fr"))
                    crmLabel.setText(rb.getString("crmLogin"));
                usernameLabel.setText(rb.getString("username"));
                passwordLabel.setText(rb.getString("password"));
                createLabel.setText(rb.getString("createAccount"));
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

        char[] userPassword = pass.toCharArray();

        ObservableList<String> encryptedPassword = FXCollections.observableArrayList();

        for (char p : userPassword){
            p += 10;
            encryptedPassword.add(String.valueOf(p));
        }

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        String formatDateTime1 = ldt.format(format);

        String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        String zone = String.valueOf(ZoneId.systemDefault());

        if (Queries.login(user, String.valueOf(encryptedPassword))) {

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("login_activity.txt", true));
                bw.write("Username: " + user + " | " + formatDateTime1 + " " + country + " " + zone + " | " +
                        "SUCCESSFUL LOGIN \n");
                bw.close();

                Queries.immediateAppointment(Queries.selectUser(user, pass));

                if (!Queries.immediateAppointment(Queries.selectUser(user, pass))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image image = new Image("/icons/Information.png");
                    stage.getIcons().add(image);
                    alert.setTitle("Upcoming Appointments");
                    alert.setContentText("No immediate upcoming appointments.");
                    alert.showAndWait();
                }

                User currentUser = new User(Queries.selectUser(user, String.valueOf(encryptedPassword)), user, pass,
                        Queries.getDoctorId(user));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
                Parent root = loader.load();
                DashboardController dashboardController = loader.getController();
                dashboardController.setUser(currentUser);
                Scene scene = new Scene(root, 1500, 800);
                scene.getStylesheets().add("/css/styles.css");
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
                Image image = new Image("/icons/Brackets_Black.png");
                stage.getIcons().add(image);
                stage.setTitle("Dashboard");
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        else if (!Queries.login(user, String.valueOf(encryptedPassword))){
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
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image image = new Image("/icons/Error.png");
                stage.getIcons().add(image);
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
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image image = new Image("/icons/Error.png");
                stage.getIcons().add(image);
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
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 600, 700));
        stage.show();
        JDBC.closeConnection();
    }

    /** Upon selecting forgot password, display the forgot password screen. **/
    public void onForgotPassword(MouseEvent mouseEvent) {

    }

}
