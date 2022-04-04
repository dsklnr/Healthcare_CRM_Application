package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Division;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Creating the reports screen controller. **/
public class ReportsController implements Initializable {
    public TableView appointmentTypeTable;
    public TableColumn countryCol;
    public TableColumn stateCol;
    public TableColumn numberOfPatientsCol;
    public TableView divisionTable;
    public TableColumn appointmentTypeCol;
    public TableColumn monthCol;
    public TableColumn totalAppointmentsCol;
    public User currentUser;
    public Label homeLabel;
    public Label patientsLabel;
    public Label scheduleLabel;
    public Label reportLabel;
    public ImageView logo;

    /** Initialize the reports controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/Brackets_White.png");
        logo.setImage(image);

        homeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        patientsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        scheduleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        reportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        ObservableList<Appointment> totalAppointments = FXCollections.observableArrayList();
        ObservableList<Division> patientsByState = FXCollections.observableArrayList();

        try {
            totalAppointments = Queries.getTotalPatientAppointments();
            patientsByState = Queries.getNumberOfPatientsByState();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        totalAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));

        divisionTable.setItems(totalAppointments);

        countryCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        numberOfPatientsCol.setCellValueFactory(new PropertyValueFactory<>("countryId"));

        appointmentTypeTable.setItems(patientsByState);

        JDBC.closeConnection();
    }

    /** Set the user object to the currently logged-in user. **/
    public void setUser(User user) {
        currentUser = user;
    }

    /** Upon clicking home, go to the dashboard screen. **/
    public void onHomeClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking patients, go to the all patients screen. **/
    public void onPatientsClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Patients");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController allAppointmentsController = loader.getController();
        allAppointmentsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

}
