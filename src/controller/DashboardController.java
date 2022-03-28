package controller;

import dao.DashboardQueries;
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
    public TableColumn<Appointment, String> typeCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> descriptionCol;
    public TableColumn<Appointment, String>  locationCol;
    public TableColumn<Appointment, String>  startTimeCol;
    public TableColumn<Appointment, String>  endTimeCol;
    public TableColumn<Appointment, Integer> contactIdCol;
    public TableColumn<Appointment, Integer> customerIdCol;
    public TableView<Appointment> dashboardTable;
    public RadioButton monthButton;
    public RadioButton weekButton;
    public ToggleGroup toggle;
    public User user;

    /** Set the user object to the currently logged-in user and populate the table with appointments within the next
     * month. **/
    public void setUser(User currentUser) throws SQLException {
        JDBC.openConnection();

        user = currentUser;

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        if (DashboardQueries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextMonthAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (DashboardQueries.getDoctorLevel(user.getUsername()) != null){
            try {
                upcomingAppointment.addAll(Queries.getNextMonthAppointments(user.getUserId()));
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

        monthButton.setSelected(true);

        appointmentIdCol.setCellValueFactory(colData -> {
            return new ReadOnlyObjectWrapper<>(colData.getValue().getAppointmentId());
        });

        typeCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getType());
        });

        titleCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getTitle());
        });

        descriptionCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getDescription());
        });

        locationCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getLocation());
        });

        startTimeCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getStartDateTime());
        });

        endTimeCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getEndDateTime());
        });

        contactIdCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getContactId());
        });

        customerIdCol.setCellValueFactory(colData ->{
            return new ReadOnlyObjectWrapper<>(colData.getValue().getCustomerId());
        });

        JDBC.closeConnection();

    }

    /** Upon clicking home, go to the dashboard screen. **/
    public void onHomeClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();

        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(user);

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking customers, go to the all customers screen. **/
    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();
        AllCustomersController customerController = loader.getController();
        customerController.setUser(user);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController appointmentsUser = loader.getController();
        appointmentsUser.setUser(user);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Appointments");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(user);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Reports");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

    }

    /** Upon clicking the month radio button, display the upcoming month's appointments. **/
    public void onMonthButton(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        monthButton.setSelected(true);

        dashboardTable.setItems(null);

        ObservableList<Appointment> upcomingAppointment = FXCollections.observableArrayList();

        if (DashboardQueries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextMonthAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (DashboardQueries.getDoctorLevel(user.getUsername()) != null){
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

        if (DashboardQueries.getDoctorLevel(user.getUsername()) == null){
            try {
                upcomingAppointment.addAll(Queries.getAllNextWeekAppointments());
                dashboardTable.setItems(upcomingAppointment);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (DashboardQueries.getDoctorLevel(user.getUsername()) != null){
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
