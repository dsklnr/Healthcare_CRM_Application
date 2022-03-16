package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** Creating the update customer controller. **/
public class UpdateCustomer implements Initializable {
    public User currentUser;
    public ComboBox countryComboBox;
    public ComboBox stateComboBox;
    public TextField customerId;
    public TextField name;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;
    public Customer selectedCustomer;
    public LocalDateTime createDate;
    public String createdBy;
    public int countryId;

    /** Initialize the update customer controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Country> allCountries = Queries.getAllCountries();
        countryComboBox.setItems(allCountries);

        JDBC.closeConnection();

    }

    /** Set the items in the state combo box. **/
    public void onAssignCountry(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        Country c = (Country) countryComboBox.getSelectionModel().getSelectedItem();

        //countryId = countryComboBox.getSelectionModel().getSelectedItem().getCountryId();
        countryId = c.getCountryId();

        ObservableList<Division> usDivisions = Queries.getAllUsStates();
        ObservableList<Division> ukCountries = Queries.getAllUkCountries();
        ObservableList<Division> canadianProvinces = Queries.getAllCanadianProvinces();

        if (countryId == 1){
            stateComboBox.setItems(usDivisions);
        }

        if (countryId == 2){
            stateComboBox.setItems(ukCountries);
        }

        if (countryId == 3){
            stateComboBox.setItems(canadianProvinces);
        }

        JDBC.closeConnection();
    }

    /** Populate the form with the selected customer's information. **/
    public void setCustomer(Customer customer) throws SQLException {
        JDBC.openConnection();

        selectedCustomer = customer;

        int id = customer.getCustomerId();
        String name = customer.getName();
        String address = customer.getAddress();
        String postal = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        //String createdDate = customer.getCreateDate();
        createdBy = customer.getCreatedBy();

        int divisionId = customer.getDivisionId();

        String state = Queries.getDivisionName(divisionId);
        int countryId = Queries.getCountryId(divisionId);
        String country = Queries.getCountryName(divisionId, countryId);

        customerId.setText(String.valueOf(id));
        this.name.setText(name);
        this.address.setText(address);
        postalCode.setText(postal);
        phoneNumber.setText(phone);
        countryComboBox.getSelectionModel().select(country);
        stateComboBox.getSelectionModel().select(state);

        ObservableList<Division> usDivisions = Queries.getAllUsStates();
        ObservableList<Division> ukCountries = Queries.getAllUkCountries();
        ObservableList<Division> canadianProvinces = Queries.getAllCanadianProvinces();

        if (countryId == 1){
            stateComboBox.setItems(usDivisions);
        }

        if (countryId == 2){
            stateComboBox.setItems(ukCountries);
        }

        if (countryId == 3){
            stateComboBox.setItems(canadianProvinces);
        }

        JDBC.closeConnection();
    }

    /** Set the user object to the currently logged-in user. **/
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /** Update the customer in the MySQL database. **/
    public void onSaveUpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        int id = Integer.parseInt(customerId.getText());
        String customerName = name.getText();
        String customerAddress = address.getText();
        String postal = postalCode.getText();
        String customerPhone = phoneNumber.getText();

        String createdBy = currentUser.getUsername();
        String create = selectedCustomer.getCreateDate();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateBy = currentUser.getUsername();
        String division = String.valueOf(stateComboBox.getSelectionModel().getSelectedItem());
        int divisionId = Queries.getDivisionId(division);

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime localUpdateTime = lastUpdate.atZone(systemZone);
        ZonedDateTime utcUpdateTime = localUpdateTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime finalUtcUpdateTime = utcUpdateTime.plusHours(1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String updateTime = finalUtcUpdateTime.format(dtf);

        Queries.updateCustomer(customerName, customerAddress, postal, customerPhone, create,
                createdBy, updateTime, lastUpdateBy, divisionId, id);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();

        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

        JDBC.closeConnection();
    }

    /** Upon selecting cancel, return to the all customers screen. **/
    public void onCancelUpdateCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();

        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }
}
