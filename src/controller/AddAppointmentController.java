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
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
    public ComboBox startHourComboBox;
    public ComboBox startMinuteComboBox;
    public ComboBox endHourComboBox;
    public ComboBox endMinuteComboBox;

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

    public void onSaveAddAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        JDBC.openConnection();

        String appointmentTitle = title.getText();
        String appointmentLocation = location.getText();
        String appointmentType = type.getText();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = currentUser.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateBy = currentUser.getUsername();
        String customerID = customerId.getText();
        String userID = userId.getText();
        String appointmentDescription = description.getText();
        String appointmentContact = String.valueOf(contactComboBox.getSelectionModel().getSelectedItem());
        int contactId = Queries.getContactId(appointmentContact);
        String startHours = String.valueOf(startHourComboBox.getSelectionModel().getSelectedItem());
        String startMinutes = String.valueOf(startMinuteComboBox.getSelectionModel().getSelectedItem());
        String endHours = String.valueOf(endHourComboBox.getSelectionModel().getSelectedItem());
        String endMinutes = String.valueOf(endMinuteComboBox.getSelectionModel().getSelectedItem());

        if (appointmentTitle == null || appointmentLocation == null || appointmentType == null || start == null ||
            end == null || startHours == null || startMinutes == null || endHours == null || endMinutes == null ||
            customerId.getText() == null || userId.getText() == null || appointmentDescription == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("One or more value(s) is missing");
            alert.showAndWait();
        }
        else {
            //"HH:mm:ss"
            DateTimeFormatter createFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
            String formatCreateDateTime = createDate.format(createFormat);
            DateTimeFormatter updateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
            String formatUpdateDateTime = lastUpdate.format(updateFormat);

            DateTimeFormatter startDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startLocalDateTime = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                    Integer.parseInt(startHours), Integer.parseInt(startMinutes));

            ZonedDateTime zdtStartLocal = ZonedDateTime.of(startLocalDateTime, ZoneId.systemDefault());
            ZonedDateTime zdtStartUtc = zdtStartLocal.withZoneSameInstant(ZoneOffset.UTC);
            String startTimeLocal = startDateFormat.format(zdtStartLocal);
            String startTimeUtc = startDateFormat.format(zdtStartUtc);

            DateTimeFormatter endDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endLocalDateTime = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                    Integer.parseInt(endHours), Integer.parseInt(endMinutes));

            ZonedDateTime zdtEndLocal = ZonedDateTime.of(endLocalDateTime, ZoneId.systemDefault());
            ZonedDateTime zdtEndUtc = zdtEndLocal.withZoneSameInstant(ZoneOffset.UTC);
            String endTimeLocal = endDateFormat.format(zdtEndLocal);
            String endTimeUtc = endDateFormat.format(zdtEndUtc);

            Queries.insertAppointment(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                    startTimeUtc, endTimeUtc, formatCreateDateTime, createdBy, formatUpdateDateTime, lastUpdateBy,
                    Integer.parseInt(customerID), Integer.parseInt(userID), contactId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            stage.setTitle("CRM Customers");
            stage.setScene(new Scene(root, 1500, 800));
            stage.show();
        }



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
