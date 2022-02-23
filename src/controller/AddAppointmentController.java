package controller;

import dao.JDBC;
import dao.Queries;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        contactComboBox.setPromptText("Select Contact");

        try {
            contactComboBox.setItems(Queries.getAllContacts());

        } catch (SQLException e) {
            e.printStackTrace();

        }

        JDBC.closeConnection();
    }

    public void onSaveAddAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        JDBC.openConnection();

        String appointmentTitle = title.getText();
        String appointmentLocation = location.getText();
        String appointmentType = type.getText();
        LocalDate start = startDate.getValue();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = currentUser.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateBy = currentUser.getUsername();
        //int customerID = Integer.parseInt(customerId.getText());
        //int userID = Integer.parseInt(userId.getText());
        String appointmentDescription = description.getText();
        String appointmentContact = String.valueOf(contactComboBox.getSelectionModel().getSelectedItem());
        int contactId = Queries.getContactId(appointmentContact);

        DateTimeFormatter createFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
        String formatCreateDateTime = createDate.format(createFormat);
        DateTimeFormatter updateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
        String formatUpdateDateTime = lastUpdate.format(updateFormat);
        DateTimeFormatter startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
        String formatStartDate = start.format(startDate);
        System.out.println(formatStartDate);


        //Queries.insertAppointment(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                //start, end, formatCreateDateTime, createdBy, formatUpdateDateTime, lastUpdateBy, customerID, userID,
                //contactId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();


        JDBC.closeConnection();
    }

    public void onCancelAddAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        stage2.setTitle("CRM Customers");
        stage2.setScene(new Scene(root, 1500, 800));
        stage2.show();
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
