package controller;

import dao.JDBC;
import dao.Queries;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    public TextField appointmentId;
    public TextField title;
    public Button onSaveAddAppointment;
    public Button onCancelAddAppointment;
    public TextField location;
    public TextField type;
    public TextField startDate;
    public TextField endDate;
    public TextField customerId;
    public TextField userId;
    public TextArea description;
    public ComboBox contactComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        contactComboBox.setPromptText("Select Contact");

        try {
            contactComboBox.setItems(Queries.getAllUsernames());

        } catch (SQLException e) {
            e.printStackTrace();

        }

        JDBC.closeConnection();
    }
}
