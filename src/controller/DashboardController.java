package controller;

import dao.DBAppointment;
import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.User;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {
    public TableColumn appointmentIdCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn startTimeCol;
    public TableColumn endTimeCol;
    public TableColumn customerIdCol;
    public TableColumn userIdCol;
    public TableView<Appointment> dashboardTable;

    ObservableList<User> currentUser = FXCollections.observableArrayList();
    private ObservableList<Appointment> fewapp = FXCollections.observableArrayList();

    public void initialize(User currentUser) throws SQLException {
        Queries.selectAppointment(currentUser.getUserId());

        dashboardTable.setItems(Queries.getAllAppointments(currentUser.getUserId()));

        //System.out.println(currentUser.getUsername());
        //System.out.println(currentUser.getPassword());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        //dashboardTable.setItems(Queries.getAllAppointments());

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        //currentUser.get(1);
    }

}
