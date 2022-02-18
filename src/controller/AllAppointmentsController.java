package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

         */
    }

    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onReportClick(MouseEvent mouseEvent) {
    }

    public void onScheduleAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Schedule An Appointment");
        stage.setScene(new Scene(root, 500, 900));
        stage.show();
    }

    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointmentScreen.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.close();
        stage.setTitle("Update An Appointment");
        stage.setScene(new Scene(root, 500, 900));
        stage.show();
    }

    public void onDeleteAppointment(ActionEvent actionEvent) {
        Appointment deleteAppointment = (Appointment) allAppointmentsTable.getSelectionModel().getSelectedItem();

        if (deleteAppointment == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Appointment");
        alert.setContentText("Are you sure you want to delete this appointment?");
        Optional <ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.YES){
            //TODO REMOVE APPOINTMENT
        }

        else{
            alert.close();
        }
    }
}