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
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class UpdateAppointmentController implements Initializable {
    public TextField appointmentId;
    public TextField title;
    public TextField location;
    public TextField type;
    public TextField customerId;
    public TextField userId;
    public TextArea description;
    public ComboBox contactComboBox;
    public User user;
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

    public void setAppointment(Appointment appointment) throws SQLException {
        JDBC.openConnection();

        selectedAppointment = appointment;

        int id = appointment.getAppointmentId();
        String appointmentTitle = appointment.getTitle();
        String appointmentDescription = appointment.getDescription();
        String appointmentLocation = appointment.getLocation();
        String appointmentType = appointment.getType();

        String createdBy = appointment.getCreatedBy();
        int customerID = appointment.getCustomerId();
        int userID = appointment.getUserId();

        appointmentId.setText(String.valueOf(id));
        title.setText(appointmentTitle);
        description.setText(appointmentDescription);
        location.setText(appointmentLocation);
        type.setText(appointmentType);
        customerId.setText(String.valueOf(customerID));
        userId.setText(String.valueOf(userID));

        String contactName = Queries.getContactName(appointment.getContactId(), appointment.getAppointmentId());
        contactComboBox.getSelectionModel().select(contactName);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime appointmentStartDateTime =
                LocalDateTime.parse(Objects.requireNonNull(Queries.getUpdateStartTime(id)), dtf);
        LocalDateTime appointmentEndDateTime =
                LocalDateTime.parse(Objects.requireNonNull(Queries.getUpdateEndTime(id)), dtf);

        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcStartTime = appointmentStartDateTime.atZone(utcZone);
        ZonedDateTime localStartTime = utcStartTime.withZoneSameInstant(ZoneOffset.systemDefault());
        ZonedDateTime finalLocalStartTime = localStartTime.minusHours(1);

        ZonedDateTime utcEndTime = appointmentEndDateTime.atZone(utcZone);
        ZonedDateTime localEndTime = utcEndTime.withZoneSameInstant(ZoneOffset.systemDefault());
        ZonedDateTime finalLocalEndTime = localEndTime.minusHours(1);

        Callback<DatePicker, DateCell> dayCellFactory = this.getDayCellFactory();
        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);

        startDate.setValue(appointmentStartDateTime.toLocalDate());
        startHourComboBox.getSelectionModel().select(finalLocalStartTime.getHour());
        startMinuteComboBox.getSelectionModel().select(finalLocalStartTime.getMinute());

        endDate.setValue(appointmentEndDateTime.toLocalDate());
        endHourComboBox.getSelectionModel().select(finalLocalEndTime.getHour());
        endMinuteComboBox.getSelectionModel().select(finalLocalEndTime.getMinute());

        JDBC.closeConnection();
    }

    public void setUser(User currentUser) {
        user = currentUser;
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


    public void onSaveAddAppointment(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        int appointmentID = Integer.parseInt(appointmentId.getText());
        String appointmentTitle = title.getText();
        String appointmentLocation = location.getText();
        String appointmentType = type.getText();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        String createDate = selectedAppointment.getCreateDate();
        String createdBy = user.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String customerID = customerId.getText();
        String userID = userId.getText();
        String appointmentDescription = description.getText();
        String appointmentContact = String.valueOf(contactComboBox.getSelectionModel().getSelectedItem());
        int contactId = Queries.getContactId(appointmentContact);
        String startHours = String.valueOf(startHourComboBox.getSelectionModel().getSelectedItem());
        String startMinutes = String.valueOf(startMinuteComboBox.getSelectionModel().getSelectedItem());
        String endHours = String.valueOf(endHourComboBox.getSelectionModel().getSelectedItem());
        String endMinutes = String.valueOf(endMinuteComboBox.getSelectionModel().getSelectedItem());
        String lastUpdateBy = user.getUsername();

        LocalDateTime startLocalDateTime = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                Integer.parseInt(startHours), Integer.parseInt(startMinutes));

        LocalDateTime endLocalDateTime = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                Integer.parseInt(endHours), Integer.parseInt(endMinutes));

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime localStartZdt = startLocalDateTime.atZone(systemZone);
        ZonedDateTime estCreateTime = localStartZdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime finalEstCreateTime = estCreateTime.plusHours(1);

        LocalTime estST = LocalTime.of(finalEstCreateTime.getHour(), finalEstCreateTime.getMinute());
        LocalTime businessOpenHour =  LocalTime.of(8, 00);
        LocalTime businessCloseHour = LocalTime.of(22, 00);

        if (appointmentTitle.equals("")|| appointmentLocation.equals("") || appointmentType.equals("") ||
                contactComboBox.getSelectionModel().getSelectedItem() == null || start == null ||
                end == null || startHourComboBox.getSelectionModel().getSelectedItem() == null ||
                startMinuteComboBox.getSelectionModel().getSelectedItem() == null ||
                endHourComboBox.getSelectionModel().getSelectedItem() == null ||
                endMinuteComboBox.getSelectionModel().getSelectedItem() == null ||
                customerId.getText().equals("") || userId.getText().equals("") || appointmentDescription.equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("One or more value(s) is missing");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }

        if (estST.isBefore(businessOpenHour) || estST.isAfter(businessCloseHour)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot set an appointment outside of business hours \n\nBusiness hours are 08:00 - 22:00 EST Monday - Friday");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }

        if (startLocalDateTime.isAfter(endLocalDateTime)){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The start date and time must be before the end date and time");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }


        else {
            ObservableList<LocalDateTime> appointmentStartDateTimes = Queries.getAllStartTimes();
            ObservableList<LocalDateTime> appointmentEndDateTimes = Queries.getAllEndTimes();

            //appointmentStartDateTimes = Queries.getAllStartTimes();
            //appointmentEndDateTimes = Queries.getAllEndTimes();
            //System.out.println(appointmentStartDateTimes);

            ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();
            ObservableList<LocalDateTime> endTimes = FXCollections.observableArrayList();

            for (LocalDateTime x : appointmentStartDateTimes) {
                int startYear = x.getYear();
                Month startMonth = x.getMonth();
                int startDay = x.getDayOfMonth();
                int startHour = x.getHour();
                int startMinute = x.getMinute();

                ZoneId utc = ZoneId.of("UTC");
                ZoneId system = ZoneId.systemDefault();

                LocalDateTime ldt = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
                ZonedDateTime zdt = ldt.atZone(utc);
                ZonedDateTime Zdt = zdt.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalZdt = Zdt.withZoneSameInstant(system).minusHours(1);

                startTimes.addAll(LocalDateTime.from(finalZdt));
                //System.out.println(finalZdt.format(dtf));
            }

            for (LocalDateTime y : appointmentEndDateTimes){
                int endYear = y.getYear();
                Month endMonth = y.getMonth();
                int endDay = y.getDayOfMonth();
                int endHour = y.getHour();
                int endMinute = y.getMinute();

                ZoneId utc = ZoneId.of("UTC");
                ZoneId system = ZoneId.systemDefault();

                LocalDateTime ldt = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
                ZonedDateTime zdt = ldt.atZone(utc);
                ZonedDateTime Zdt = zdt.withZoneSameInstant(ZoneOffset.systemDefault());
                ZonedDateTime finalZdt = Zdt.withZoneSameInstant(system).minusHours(1);

                endTimes.addAll(LocalDateTime.from(finalZdt));
                //System.out.println(finalZdt.format(dtf));
            }

            DateTimeFormatter dateTF = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");

            for (int i = 0; i < startTimes.size(); i++) {
                LocalDateTime currentStart = startTimes.get(i);
                LocalDateTime currentEnd = endTimes.get(i);
                LocalDateTime myDT = LocalDateTime.of(2020, 05, 29, 5, 00);

                if (startLocalDateTime.isAfter(currentStart) && startLocalDateTime.isBefore(currentEnd)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    Optional<ButtonType> action = alert.showAndWait();

                    if (action.get() == ButtonType.OK) {
                        return;
                    }
                }

                if (startLocalDateTime.equals(currentStart) || startLocalDateTime.equals(currentEnd)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    Optional<ButtonType> action = alert.showAndWait();

                    if (action.get() == ButtonType.OK) {
                        return;
                    }
                }
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ZonedDateTime zdtStartLocal = ZonedDateTime.of(startLocalDateTime, ZoneId.systemDefault());
            ZonedDateTime zdtStartUtc = zdtStartLocal.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime finalZdtStartUtc = zdtStartUtc.plusHours(1);
            String startTimeUtc = finalZdtStartUtc.format(dtf);

            ZonedDateTime zdtEndLocal = ZonedDateTime.of(endLocalDateTime, ZoneId.systemDefault());
            ZonedDateTime zdtEndUtc = zdtEndLocal.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime finalZdtEndUtc = zdtEndUtc.plusHours(1);
            String endTimeUtc = finalZdtEndUtc.format(dtf);

            ZonedDateTime localUpdateTime = lastUpdate.atZone(systemZone);
            ZonedDateTime utcUpdateTime = localUpdateTime.withZoneSameInstant(ZoneOffset.UTC);
            String updateTime = utcUpdateTime.format(dtf);

            /*
            ZonedDateTime localCreateTime = createDate.atZone(systemZone);
            ZonedDateTime utcCreateTime = localCreateTime.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime finalUtcCreateTime = utcCreateTime;
            String createTime = utcCreateTime.format(dtf);

             */

            Queries.updateAppointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                    startTimeUtc, endTimeUtc, createDate, createdBy, updateTime, lastUpdateBy,
                    Integer.parseInt(customerID), Integer.parseInt(userID), contactId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            AllAppointmentsController appointmentsUser = loader.getController();
            appointmentsUser.setUser(user);

            stage.close();
            stage.setTitle("Appointments");
            stage.setScene(new Scene(root, 1500, 800));
            stage.show();
        }

        JDBC.closeConnection();
    }

    public void onCancelAddAppointment(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();

        AllAppointmentsController appointmentsUser = loader.getController();
        appointmentsUser.setUser(user);

        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        stage2.setTitle("Appointments");
        stage2.setScene(new Scene(root, 1500, 800));
        stage2.show();
    }

}
