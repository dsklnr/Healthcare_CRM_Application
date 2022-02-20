package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Division;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> stateComboBox;
    public TextField name;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Country> allCountries = Queries.getAllCountries();
        ObservableList<Division> allDivisions = Queries.getAllDivisions();

        countryComboBox.setItems(allCountries);
        countryComboBox.setPromptText("Select A Country");
        stateComboBox.setItems(allDivisions);
        stateComboBox.setPromptText("Select State or Province");
        
        
        JDBC.closeConnection();
    }


    public String toString(){
        //ObservableList<Customer> customers = Queries.getAllCustomers();
        return (getClass().getName());
        //return (getClass().getName().toString());

    }



    public void onSaveAddCustomer(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        String customerName = name.getText();
        String customerAddress = address.getText();
        String postal = postalCode.getText();
        String customerPhone = phoneNumber.getText();
        LocalDateTime createDate = LocalDateTime.now();
        //String createdBy = Queries
        LocalDateTime lastUpdate = LocalDateTime.now();
        //String lastUpdateBy = Queries
        //int divisionId = stateComboBox.getSelectionModel().getSelectedItem().getDivisionId();

        DateTimeFormatter createFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
        String formatCreateDateTime = createDate.format(createFormat);
        DateTimeFormatter updateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
        String formatUpdateDateTime = lastUpdate.format(updateFormat);

        System.out.println(formatCreateDateTime);
        System.out.println(formatUpdateDateTime);

        System.out.println(System.getProperty("user.name"));


        //Queries.insertCustomer();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        JDBC.closeConnection();
    }

    public void onCancelAddCustomer(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
