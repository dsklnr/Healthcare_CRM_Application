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
    public TableColumn customerIdCol;
    public TableColumn userIdCol;
    public TableColumn contactIdCol;
    public TableView allAppointmentsTable;
    public TextField searchAppointments;
    private User currentUser;

    /** Initialize the all appointments controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

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
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
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
    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardUser = loader.getController();
        dashboardUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking customers, go to the all customers screen. **/
    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();
        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController appointmentsUser = loader.getController();
        appointmentsUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Reports");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking the schedule button, go to the add appointment screen. **/
    public void onScheduleAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        Parent root = loader.load();

        AddAppointmentController appointmentsUser = loader.getController();
        appointmentsUser.setUser(currentUser);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
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

            UpdateAppointmentController updateAppointmentScreen = loader.getController();
            updateAppointmentScreen.setAppointment(appointment);
            updateAppointmentScreen.setUser(currentUser);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
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
            alert.setTitle("Confirm Delete Appointment");
            alert.setContentText("Are you sure you want to delete this appointment?");
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
