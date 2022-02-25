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
import javafx.util.Callback;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

        Callback<DatePicker, DateCell> dayCellFactory = this.getDayCellFactory();
        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);

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

    private Callback<DatePicker, DateCell> getDayCellFactory(){

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);

                        if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY){
                            setDisable(true);
                        }
                    }
                };
            }
        };
        return dayCellFactory;
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

            ObservableList<LocalDateTime> appointmentStartDateTimes = FXCollections.observableArrayList();
            ObservableList<LocalDateTime> appointmentEndDateTimes = FXCollections.observableArrayList();

            appointmentStartDateTimes = Queries.getAllStartTimes();
            appointmentEndDateTimes = Queries.getAllEndTimes();

            ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();
            ObservableList<LocalDateTime> endTimes = FXCollections.observableArrayList();

            for (LocalDateTime x : appointmentStartDateTimes) {
                int startYear = x.getYear();
                Month startMonth = x.getMonth();
                int startDay = x.getDayOfMonth();
                int startHour = x.getHour();
                int startMinute = x.getMinute();

                LocalDateTime startLDT = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
                startTimes.addAll(startLDT);
            }

            for (LocalDateTime y : appointmentEndDateTimes){
                int endYear = y.getYear();
                Month endMonth = y.getMonth();
                int endDay = y.getDayOfMonth();
                int endHour = y.getHour();
                int endMinute = y.getMinute();

                LocalDateTime endLDT = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
                endTimes.addAll(endLDT);
            }

            LocalDateTime startLocalDateTime = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                    Integer.parseInt(startHours), Integer.parseInt(startMinutes));

            LocalDateTime endLocalDateTime = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                    Integer.parseInt(endHours), Integer.parseInt(endMinutes));

            DateTimeFormatter dateTF = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");

            for (int i = 0; i < startTimes.size(); i++){
                LocalDateTime currentStart = startTimes.get(i);
                LocalDateTime currentEnd = endTimes.get(i);

                LocalDateTime myDT = LocalDateTime.of(startLocalDateTime.getYear(), startLocalDateTime.getMonth(),
                        startLocalDateTime.getDayOfMonth(), startLocalDateTime.getHour(), startLocalDateTime.getMinute());

                if (myDT.isAfter(currentStart) && myDT.isBefore(currentEnd)){

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlapping Appointment");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    alert.showAndWait();
                }

                if (myDT.equals(currentStart) || myDT.equals(currentEnd)){

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Overlapping Appointment");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    alert.showAndWait();
                }

                else {
                    DateTimeFormatter databaseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
                    String formatCreateDateTime = createDate.format(databaseFormat);
                    String formatUpdateDateTime = lastUpdate.format(databaseFormat);

                    ZonedDateTime zdtStartLocal = ZonedDateTime.of(startLocalDateTime, ZoneId.systemDefault());
                    ZonedDateTime zdtStartUtc = zdtStartLocal.withZoneSameInstant(ZoneOffset.UTC);
                    String startTimeLocal = databaseFormat.format(zdtStartLocal);
                    String startTimeUtc = databaseFormat.format(zdtStartUtc);

                    ZonedDateTime zdtEndLocal = ZonedDateTime.of(endLocalDateTime, ZoneId.systemDefault());
                    ZonedDateTime zdtEndUtc = zdtEndLocal.withZoneSameInstant(ZoneOffset.UTC);
                    String endTimeUtc = databaseFormat.format(zdtEndUtc);

                    JDBC.openConnection();

                    Queries.insertAppointment(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                            startTimeUtc, endTimeUtc, formatCreateDateTime, createdBy, formatUpdateDateTime, lastUpdateBy,
                            Integer.parseInt(customerID), Integer.parseInt(userID), contactId);

                    JDBC.closeConnection();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Appointments");
                    stage.setScene(new Scene(root, 1500, 800));
                    stage.show();
                }
            }
        }



        JDBC.closeConnection();
    }

    public void onCancelAddAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        stage2.setTitle("Appointments");
        stage2.setScene(new Scene(root, 1500, 800));
        stage2.show();
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
