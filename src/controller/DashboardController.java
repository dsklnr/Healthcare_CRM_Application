package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public TableColumn appointmentIdCol;
    public TableColumn typeCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn startTimeCol;
    public TableColumn endTimeCol;
    public TableView<Appointment> dashboardTable;

    //ObservableList<User> currentUser = FXCollections.observableArrayList();
    //private ObservableList<Appointment> fewapp = FXCollections.observableArrayList();

    public void initialize(User currentUser) throws SQLException {
        //Queries.selectAppointment(currentUser.getUserId());
        JDBC.openConnection();

        dashboardTable.setItems(Queries.getAllAppointmentsFromId(currentUser.getUserId()));

        JDBC.closeConnection();

        //System.out.println(currentUser.getUsername());
        //System.out.println(currentUser.getPassword());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        //dashboardTable.setItems(Queries.getAllAppointments());

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        JDBC.closeConnection();

        //currentUser.get(1);
    }

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        //DashboardController dashboardUser = loader.getController();
        //dashboardUser.initialize(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

    }

    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

         */
    }
}
