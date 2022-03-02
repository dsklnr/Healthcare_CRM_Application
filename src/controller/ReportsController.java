package controller;

import dao.JDBC;
import dao.ReportQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Division;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    public TableView appointmentTypeTable;
    public TableColumn countryCol;
    public TableColumn stateCol;
    public TableColumn numberOfCustomersCol;
    public TableView divisionTable;
    public TableColumn appointmentTypeCol;
    public TableColumn monthCol;
    public TableColumn totalAppointmentsCol;
    public TableView contactScheduleTable;
    public TableColumn customerIdCol;
    public TableColumn appointmentIdCol;
    public TableColumn titleCol;
    public TableColumn typeCol;
    public TableColumn descriptionCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Appointment> totalAppointments = FXCollections.observableArrayList();
        ObservableList<Division> customersByState = FXCollections.observableArrayList();
        ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();

        try {
            totalAppointments = ReportQueries.getTotalCustomerAppointments();
            customersByState = ReportQueries.getNumberOfCustomersByState();
            contactSchedule = ReportQueries.getContactSchedule();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        totalAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));

        divisionTable.setItems(totalAppointments);

        countryCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        numberOfCustomersCol.setCellValueFactory(new PropertyValueFactory<>("countryId"));

        appointmentTypeTable.setItems(customersByState);

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        contactScheduleTable.setItems(contactSchedule);

        JDBC.closeConnection();
    }

    public void setUser(User user) {
        currentUser = user;
    }

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();
        CustomerController customerController = loader.getController();
        customerController.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController appointmentsController = loader.getController();
        appointmentsController.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Appointments");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

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

}
