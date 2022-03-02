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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class DashboardController implements Initializable {
    public TableColumn appointmentIdCol;
    public TableColumn typeCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn startTimeCol;
    public TableColumn endTimeCol;
    public TableView<Appointment> dashboardTable;
    public RadioButton monthButton;
    public RadioButton weekButton;
    public ToggleGroup toggle;
    static User user;

    //ObservableList<User> currentUser = FXCollections.observableArrayList();
    //private ObservableList<Appointment> fewapp = FXCollections.observableArrayList();

    public void setUser(User currentUser) {
        JDBC.openConnection();

        user = currentUser;

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        try {
            upcomingAppointment.addAll(Queries.getNextMonthAppointments(user.getUserId()));
            dashboardTable.setItems(upcomingAppointment);

        }catch (Exception ex){
            ex.printStackTrace();
        }


        JDBC.closeConnection();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        monthButton.setSelected(true);

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));


        //dashboardTable.setItems(Queries.getNextMonthAppointments(user.getUserId()));


        JDBC.closeConnection();

        //currentUser.get(1);
    }

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(user);
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
        customerController.setUser(user);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController appointmentsUser = loader.getController();
        appointmentsUser.setUser(user);
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
        reportsController.setUser(user);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Reports");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

    }


    public void onMonthButton(ActionEvent actionEvent) {
        JDBC.openConnection();

        monthButton.setSelected(true);

        dashboardTable.setItems(null);

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        try {
            upcomingAppointment.addAll(Queries.getNextMonthAppointments(user.getUserId()));
            dashboardTable.setItems(upcomingAppointment);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        JDBC.closeConnection();

    }



    public void onWeekButton(ActionEvent actionEvent) {
        JDBC.openConnection();

        weekButton.setSelected(true);

        try {
            dashboardTable.setItems(Queries.getNextWeekAppointments(user.getUserId()));
            System.out.println(user.getUserId());

        }catch (Exception ex){
            ex.printStackTrace();
        }

        JDBC.closeConnection();
    }

}
