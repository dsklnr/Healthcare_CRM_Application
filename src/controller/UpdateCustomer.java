package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import model.Country;
import model.Customer;
import model.Division;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Country> allCountries = Queries.getAllCountries();
        ObservableList<Division> allDivisions = Queries.getAllDivisions();

        countryComboBox.setItems(allCountries);
        stateComboBox.setItems(allDivisions);

        JDBC.closeConnection();

    }

    public void setCustomer(Customer customer) throws SQLException {
        JDBC.openConnection();

        selectedCustomer = customer;

        int id = customer.getCustomerId();
        String name = customer.getName();
        String address = customer.getAddress();
        String postal = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        String createDate = customer.getCreateDate();
        String createdBy = customer.getCreatedBy();
        String lastUpdate = customer.getLastUpdate();
        String lastUpdateBy = customer.getLastUpdateBy();
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

        JDBC.closeConnection();
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void onSaveUpdateCustomer(ActionEvent actionEvent) {
    }

    public void onCancelUpdateCustomer(ActionEvent actionEvent) {
    }
}
