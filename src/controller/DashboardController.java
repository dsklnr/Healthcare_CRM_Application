package controller;

import dao.JDBC;
import dao.Queries;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

/** Creating the dashboard controller. **/
public class DashboardController implements Initializable {
    public TableColumn<Appointment, Integer> appointmentIdCol;
    public TableColumn<Appointment, Integer> patientIdCol;
    public TableColumn<Appointment, String> patientNameCol;
    public TableColumn<Appointment, Integer> doctorIdCol;
    public TableColumn<Appointment, String> contactNameCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> typeCol;
    public TableColumn<Appointment, String> locationCol;
    public TableColumn<Appointment, String> startCol;
    public TableColumn<Appointment, String> endCol;
    public TableColumn<Appointment, String> descriptionCol;
    public TableView<Appointment> dashboardTable;
    public RadioButton monthButton;
    public RadioButton weekButton;
    public ToggleGroup toggle;
    public User user;
    public ImageView logo;
    public Label homeLabel;
    public Label patientLabel;
    public Label scheduleLabel;
    public Label reportLabel;

    /** Set the user object to the currently logged-in user and populate the table with appointments within the next
     * month. **/
    public void setUser(User currentUser) throws SQLException {
        JDBC.openConnection();

        user = currentUser;

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        if (Queries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextMonthAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (Queries.getDoctorLevel(user.getUsername()) != null){
            try {
                upcomingAppointment.addAll(Queries.getNextMonthAppointments(user.getDoctorId()));
                dashboardTable.setItems(upcomingAppointment);

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


        JDBC.closeConnection();

    }

    /** Initialize the dashboard controller.
     *
     * lambdas are used to populate cells based on appointment values.
     * **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/Brackets_White.png");
        logo.setImage(image);

        homeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        patientLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        scheduleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        reportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");


        monthButton.setSelected(true);

        appointmentIdCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getAppointmentId());
        });

        patientIdCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getPatientID());
        });

        patientNameCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getCreateDate());
        });

        doctorIdCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getDoctorId());
        });

        contactNameCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getCreatedBy());
        });

        titleCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getTitle());
        });

        typeCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getType());
        });

        locationCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getLocation());
        });

        startCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getStartDateTime());
        });

        endCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getEndDateTime());
        });

        descriptionCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getDescription());
        });

        JDBC.closeConnection();

    }

    /** Upon clicking home, go to the dashboard screen. **/
    public void onHomeClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(user);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking patients, go to the all patients screen. **/
    public void onPatientsClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(user);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Patients");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController allAppointmentsController = loader.getController();
        allAppointmentsController.setUser(user);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(user);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();

    }

    /** Upon clicking the month radio button, display the upcoming month's appointments. **/
    public void onMonthButton(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        monthButton.setSelected(true);

        dashboardTable.setItems(null);

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        if (Queries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextMonthAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (Queries.getDoctorLevel(user.getUsername()) != null){
            try {
                upcomingAppointment.addAll(Queries.getNextMonthAppointments(user.getUserId()));
                dashboardTable.setItems(upcomingAppointment);

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


    }

    /** Upon clicking the week radio button, display the upcoming week's appointments. **/
    public void onWeekButton(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        weekButton.setSelected(true);

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        if (Queries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextWeekAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (Queries.getDoctorLevel(user.getUsername()) != null){
            try {
                upcomingAppointment.addAll(Queries.getNextWeekAppointments(user.getUserId()));
                dashboardTable.setItems(upcomingAppointment);

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        JDBC.closeConnection();
    }

}
