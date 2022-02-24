package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Appointment;
import model.User;

import javax.swing.text.DateFormatter;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdateAppointmentScreen implements Initializable {
    public TextField appointmentId;
    public TextField title;
    public TextField location;
    public TextField type;
    public TextField customerId;
    public TextField userId;
    public TextArea description;
    public ComboBox contactComboBox;
    public User currentUser;
    public DatePicker startDate;
    public DatePicker endDate;
    public ComboBox startHourComboBox;
    public ComboBox startMinuteComboBox;
    public ComboBox endHourComboBox;
    public ComboBox endMinuteComboBox;
    public Appointment selectedAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<String> hours = FXCollections.observableArrayList();
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        ObservableList<String> minutes = FXCollections.observableArrayList();
        minutes.addAll("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");


        contactComboBox.setPromptText("Select Contact");
        startHourComboBox.setItems(hours);
        endHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endMinuteComboBox.setItems(minutes);

        try {
            contactComboBox.setItems(Queries.getAllContacts());

        } catch (SQLException e) {
            e.printStackTrace();

        }

        JDBC.closeConnection();
    }

    public void setAppointment(Appointment appointment) {
        selectedAppointment = appointment;

        int id = appointment.getAppointmentId();
        String appointmentTitle = appointment.getTitle();
        String appointmentDescription = appointment.getDescription();
        String appointmentLocation = appointment.getLocation();
        String appointmentType = appointment.getType();
        int customerID = appointment.getCustomerId();
        int userID = appointment.getUserId();

        appointmentId.setText(String.valueOf(id));
        title.setText(appointmentTitle);
        description.setText(appointmentDescription);
        location.setText(appointmentLocation);
        type.setText(appointmentType);
        customerId.setText(String.valueOf(customerID));
        userId.setText(String.valueOf(userID));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime appointmentStartDateTime = LocalDateTime.parse(appointment.getStartDateTime(), dtf);
        LocalDateTime appointmentEndDateTime = LocalDateTime.parse(appointment.getEndDateTime(), dtf);

        startDate.setValue(appointmentStartDateTime.toLocalDate());
        startHourComboBox.getSelectionModel().select(appointmentStartDateTime.getHour());
        startMinuteComboBox.getSelectionModel().select(appointmentStartDateTime.getMinute());

        endDate.setValue(appointmentEndDateTime.toLocalDate());
        endHourComboBox.getSelectionModel().select(appointmentEndDateTime.getHour());
        endMinuteComboBox.getSelectionModel().select(appointmentEndDateTime.getMinute());



    }

    public void onSaveAddAppointment(ActionEvent actionEvent) {
    }

    public void onCancelAddAppointment(ActionEvent actionEvent) {
    }


}
