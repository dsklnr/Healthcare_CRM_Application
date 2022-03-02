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
import javafx.scene.control.ListCell;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
    public LocalDateTime createDate;
    public String createdBy;

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

        JDBC.closeConnection();
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();

        CustomerController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();

        JDBC.closeConnection();
    }

    public void onCancelUpdateCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();

        CustomerController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }
}
