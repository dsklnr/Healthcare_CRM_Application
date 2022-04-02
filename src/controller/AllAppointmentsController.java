package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Creating the all appointments controller. **/
public class AllAppointmentsController implements Initializable {
    public TableColumn appointmentIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn typeCol;
    public TableColumn startTimeCol;
    public TableColumn endTimeCol;
    public TableColumn createDateCol;
    public TableColumn createdByCol;
    public TableColumn lastUpdateCol;
    public TableColumn lastUpdatedByCol;
    public TableColumn patientIdCol;
    public TableColumn userIdCol;
    public TableColumn contactIdCol;
    public TableView allAppointmentsTable;
    public TextField searchAppointments;
    public Label homeLabel;
    public Label patientsLabel;
    public Label scheduleLabel;
    public Label reportLabel;
    public ImageView logo;
    private User currentUser;

    /** Initialize the all appointments controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/Brackets_White.png");
        logo.setImage(image);

        homeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        patientsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        scheduleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        reportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        allAppointmentsTable.setItems(Queries.getAllAppointments());

        JDBC.closeConnection();

    }

    /** Set the user object to the currently logged-in user. **/
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
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

    /** Upon clicking the schedule button, go to the add appointment screen. **/
    public void onScheduleAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        Parent root = loader.load();
        AddAppointmentController addAppointmentController = loader.getController();
        addAppointmentController.setUser(currentUser);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Schedule An Appointment");
        stage.setScene(new Scene(root, 800, 900));
        stage.show();
    }

    /** Upon clicking the update button, go to the update appointment screen and populate the form based on the
     * appointment selected. **/
    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        JDBC.openConnection();

        Appointment appointment = (Appointment) allAppointmentsTable.getSelectionModel().getSelectedItem();

        if (appointment != null){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointmentScreen.fxml"));
            Parent root = loader.load();
            UpdateAppointmentController updateAppointmentController = loader.getController();
            updateAppointmentController.setAppointment(appointment);
            updateAppointmentController.setUser(currentUser);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            Image image = new Image("/icons/Brackets_Black.png");
            stage.getIcons().add(image);
            stage.setTitle("Update An Appointment");
            stage.setScene(new Scene(root, 800, 900));
            stage.show();
        }

        JDBC.closeConnection();
    }

    /** Upon clicking the delete button, delete the selected appointment. **/
    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        Appointment appointment = (Appointment) allAppointmentsTable.getSelectionModel().getSelectedItem();

        if (appointment != null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Confirmation.png");
            stage.getIcons().add(image);
            alert.setTitle("Confirm Delete Appointment");
            alert.setContentText("Are you sure you want to delete appointment ID: " + appointment.getAppointmentId() +
                    ", appointment type: " + appointment.getType() + "?");
            Optional <ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK){
                Queries.deleteAppointment(appointment.getAppointmentId());
                allAppointmentsTable.setItems(Queries.getAllAppointments());
            }

        }

        JDBC.closeConnection();
    }

    /** Upon typing in the search bar, display the searched appointment ID. **/
    public void onSearchAppointments(ActionEvent actionEvent) {
        JDBC.openConnection();

        ObservableList<Appointment> allAppointments = Queries.getAllAppointments();
        ObservableList<Appointment> appointmentSearch = FXCollections.observableArrayList();

        for (int i = 0; i < allAppointments.size(); i++){
            Appointment currentAppointment = allAppointments.get(i);

            if (String.valueOf(currentAppointment.getAppointmentId()).contains(searchAppointments.getText())){
                appointmentSearch.addAll(currentAppointment);
            }
        }

        allAppointmentsTable.setItems(appointmentSearch);

        JDBC.closeConnection();

    }
}
