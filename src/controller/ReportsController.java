package controller;

import dao.JDBC;
import dao.ReportQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Appointment;
import model.Division;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Appointment> appointment = FXCollections.observableArrayList();
        ObservableList<Division> division = FXCollections.observableArrayList();

        try {
            appointment = ReportQueries.getTotalCustomerAppointments();
            division = ReportQueries.getNumberOfCustomersByState();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        totalAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));

        divisionTable.setItems(appointment);

        countryCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        numberOfCustomersCol.setCellValueFactory(new PropertyValueFactory<>("countryId"));

        appointmentTypeTable.setItems(division);




        JDBC.closeConnection();
    }

    public void onHomeClick(MouseEvent mouseEvent) {
    }

    public void onCustomersClick(MouseEvent mouseEvent) {
    }

    public void onScheduleClick(MouseEvent mouseEvent) {
    }

    public void onReportClick(MouseEvent mouseEvent) {
    }
}
