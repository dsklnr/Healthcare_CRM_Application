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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** Creating the add appointment controller. **/
public class  AddAppointmentController implements Initializable {
    public TextField appointmentId;
    public TextField title;
    public TextField location;
    public TextField type;
    public TextArea description;
    public ComboBox userIdComboBox;
    public ComboBox contactComboBox;
    public ComboBox patientComboBox;
    public DatePicker startDate;
    public DatePicker endDate;
    public ComboBox startHourComboBox;
    public ComboBox startMinuteComboBox;
    public ComboBox endHourComboBox;
    public ComboBox endMinuteComboBox;
    public User user;


    /** Initialize the add appointment controller. **/
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

        userIdComboBox.setPromptText("Select Doctor ID");
        contactComboBox.setPromptText("Select Doctor");
        patientComboBox.setPromptText("Select Patient");
        startHourComboBox.setItems(hours);
        endHourComboBox.setItems(hours);
        startMinuteComboBox.setItems(minutes);
        endMinuteComboBox.setItems(minutes);

        try {
            userIdComboBox.setItems(Queries.getAllUsers());
            contactComboBox.setItems(Queries.getAllContactInfo());
            patientComboBox.setItems(Queries.getAllPatients());

        } catch (SQLException e) {
            e.printStackTrace();

        }

        JDBC.closeConnection();
    }

    /** Remove Saturday & Sunday from the date picker.
     * @return Returns a date picker where Saturday and Sunday are disabled. **/
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

    /** Set the user object to the currently logged-in user. **/
    public void setUser (User currentUser){
        user = currentUser;
    }

    /** Insert the new appointment into the MySQL database. **/
    public void onSaveAddAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        JDBC.openConnection();

        String appointmentTitle = title.getText();
        String appointmentLocation = location.getText();
        String appointmentType = type.getText();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = user.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateBy = user.getUsername();
        //String patientID = patientId.getText();
        //String userID = userId.getText();
        String appointmentDescription = description.getText();
        int appointmentUser = Integer.parseInt(String.valueOf(userIdComboBox.getSelectionModel().getSelectedItem()));
        String appointmentContact = String.valueOf(contactComboBox.getSelectionModel().getSelectedItem());
        int contactId = Queries.getOnlyDoctorId(appointmentContact);
        String appointmentPatient = String.valueOf(patientComboBox.getSelectionModel().getSelectedItem());
        int patientId =Queries.getPatientId(appointmentPatient);
        String startHours = String.valueOf(startHourComboBox.getSelectionModel().getSelectedItem());
        String startMinutes = String.valueOf(startMinuteComboBox.getSelectionModel().getSelectedItem());
        String endHours = String.valueOf(endHourComboBox.getSelectionModel().getSelectedItem());
        String endMinutes = String.valueOf(endMinuteComboBox.getSelectionModel().getSelectedItem());

        LocalDateTime startLocalDateTime = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                Integer.parseInt(startHours), Integer.parseInt(startMinutes));

        LocalDateTime endLocalDateTime = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                Integer.parseInt(endHours), Integer.parseInt(endMinutes));

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime localStartZdt = startLocalDateTime.atZone(systemZone);
        ZonedDateTime estCreateTime = localStartZdt.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalTime estST = LocalTime.of(estCreateTime.getHour(), estCreateTime.getMinute());
        LocalTime businessOpenHour =  LocalTime.of(8, 00);
        LocalTime businessCloseHour = LocalTime.of(22, 00);

        if (appointmentTitle.equals("")|| appointmentLocation.equals("") || appointmentType.equals("") ||
                userIdComboBox.getSelectionModel().getSelectedItem() == null ||
                contactComboBox.getSelectionModel().getSelectedItem() == null ||
                patientComboBox.getSelectionModel().getSelectedItem() == null || start == null ||
                end == null || startHourComboBox.getSelectionModel().getSelectedItem() == null ||
                startMinuteComboBox.getSelectionModel().getSelectedItem() == null ||
                endHourComboBox.getSelectionModel().getSelectedItem() == null ||
                endMinuteComboBox.getSelectionModel().getSelectedItem() == null ||
                appointmentDescription.equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Error.png");
            stage.getIcons().add(image);
            alert.setTitle("ERROR");
            alert.setContentText("One or more value(s) is missing");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }

        if (estST.isBefore(businessOpenHour) || estST.isAfter(businessCloseHour)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Error.png");
            stage.getIcons().add(image);
            alert.setTitle("Error");
            alert.setContentText("Cannot set an appointment outside of business hours \n\nBusiness hours are 08:00 - 22:00 EST Monday - Friday");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }

        if (startLocalDateTime.isAfter(endLocalDateTime)){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Error.png");
            stage.getIcons().add(image);
            alert.setTitle("Error");
            alert.setContentText("The start date and time must be before the end date and time");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK) {
                return;
            }
        }

        if (startLocalDateTime.equals(endLocalDateTime)){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Error.png");
            stage.getIcons().add(image);
            alert.setTitle("Error");
            alert.setContentText("The start time cannot be the same as the end time");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK){
                return;
            }
        }

        else {
            ObservableList<LocalDateTime> appointmentStartDateTimes = Queries.getAllStartTimes(String.valueOf(appointmentUser));
            ObservableList<LocalDateTime> appointmentEndDateTimes = Queries.getAllEndTimes(String.valueOf(appointmentUser));

            ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();
            ObservableList<LocalDateTime> endTimes = FXCollections.observableArrayList();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            //ZonedDateTime localStart = startLocalDateTime.atZone(ZoneId.of("UTC"));
            //ZonedDateTime utcStart = localStart.withZoneSameInstant(ZoneId.systemDefault());
            //LocalDateTime addStart = utcStart.toLocalDateTime();

            for (LocalDateTime x : appointmentStartDateTimes) {
                int startYear = x.getYear();
                Month startMonth = x.getMonth();
                int startDay = x.getDayOfMonth();
                int startHour = x.getHour();
                int startMinute = x.getMinute();

                ZoneId utc = ZoneId.of("UTC");

                LocalDateTime ldt = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
                ZonedDateTime zdt = ldt.atZone(utc);
                ZonedDateTime Zdt = zdt.withZoneSameInstant(ZoneId.systemDefault());

                //startTimes.addAll(LocalDateTime.from(Zdt));
                startTimes.addAll(ldt);
            }

            for (LocalDateTime y : appointmentEndDateTimes){
                int endYear = y.getYear();
                Month endMonth = y.getMonth();
                int endDay = y.getDayOfMonth();
                int endHour = y.getHour();
                int endMinute = y.getMinute();

                ZoneId utc = ZoneId.of("UTC");

                LocalDateTime ldt = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
                ZonedDateTime zdt = ldt.atZone(utc);
                ZonedDateTime Zdt = zdt.withZoneSameInstant(ZoneId.systemDefault());

                //endTimes.addAll(LocalDateTime.from(Zdt));
                endTimes.addAll(ldt);
            }

            DateTimeFormatter dateTF = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");

            for (int i = 0; i < startTimes.size(); i++) {
                LocalDateTime currentStart = startTimes.get(i);
                LocalDateTime currentEnd = endTimes.get(i);
                //LocalDateTime myDT = LocalDateTime.of(2020, 05, 29, 5, 00);

                if (startLocalDateTime.isAfter(currentStart) && startLocalDateTime.isBefore(currentEnd)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image image = new Image("/icons/Error.png");
                    stage.getIcons().add(image);
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
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image image = new Image("/icons/Error.png");
                    stage.getIcons().add(image);
                    alert.setTitle("ERROR");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    Optional<ButtonType> action = alert.showAndWait();

                    if (action.get() == ButtonType.OK) {
                        return;
                    }
                }

                if (startLocalDateTime.isBefore(currentEnd) && endLocalDateTime.isAfter(currentStart)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image image = new Image("/icons/Error.png");
                    stage.getIcons().add(image);
                    alert.setTitle("ERROR");
                    alert.setContentText("Your date and time overlaps another appointment starting at " +
                            currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
                    Optional<ButtonType> action = alert.showAndWait();

                    if (action.get() == ButtonType.OK) {
                        return;
                    }
                }
            }


            DateTimeFormatter databaseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ZonedDateTime utcStartTime = localStartZdt.withZoneSameInstant(ZoneId.of("UTC"));
            String finalStartTime = utcStartTime.format(databaseFormat);

            ZonedDateTime localEndZdt = endLocalDateTime.atZone(systemZone);
            ZonedDateTime utcEndTime = localEndZdt.withZoneSameInstant(ZoneId.of("UTC"));
            String finalEndTime = utcEndTime.format(databaseFormat);

            ZonedDateTime localCreateTime = createDate.atZone(systemZone);
            ZonedDateTime utcCreateTime = localCreateTime.withZoneSameInstant(ZoneId.of("UTC"));
            String finalCreateTime = utcCreateTime.format(databaseFormat);

            ZonedDateTime localUpdateTime = lastUpdate.atZone(systemZone);
            ZonedDateTime utcUpdateTime = localUpdateTime.withZoneSameInstant(ZoneId.of("UTC"));
            String finalUpdateTime = utcUpdateTime.format(databaseFormat);

            JDBC.openConnection();

            Queries.insertAppointment(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                   finalStartTime, finalEndTime, finalCreateTime, createdBy, finalUpdateTime, lastUpdateBy,
                    patientId, appointmentUser, contactId);

            JDBC.closeConnection();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
            Parent root = loader.load();
            AllAppointmentsController allAppointmentsController = loader.getController();
            allAppointmentsController.setUser(user);
            Scene scene = new Scene(root, 1500, 800);
            scene.getStylesheets().add("/css/styles.css");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Image image = new Image("/icons/Brackets_Black.png");
            stage.getIcons().add(image);
            stage.setTitle("Appointments");
            stage.setScene(scene);
            stage.show();
        }

        JDBC.closeConnection();
    }

    /** Upon selecting cancel, return to the all appointments screen. **/
    public void onCancelAddAppointment (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController allAppointmentsController = loader.getController();
        allAppointmentsController.setUser(user);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage2.getIcons().add(image);
        stage2.setTitle("Appointments");
        stage2.setScene(scene);
        stage2.show();
    }
}
