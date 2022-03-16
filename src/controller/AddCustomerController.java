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

/** Creating the add customer controller. **/
public class AddCustomerController implements Initializable {
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> stateComboBox;
    public TextField name;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;
    public User currentUser;
    public int countryId;

    /** Initialize the add customer controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

            ObservableList<Country> allCountries = Queries.getAllCountries();
            countryComboBox.setItems(allCountries);
            countryComboBox.getSelectionModel().clearSelection();
            countryComboBox.setPromptText("Select A Country");
            stateComboBox.setPromptText("Select State or Province");

        JDBC.closeConnection();
    }

    /** Set the user object to the currently logged-in user. **/
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /** Set the items in the state combo box. **/
    public void onAssignCountry(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();

        countryId = countryComboBox.getSelectionModel().getSelectedItem().getCountryId();

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

    /** Insert a new customer into the database. **/
    public void onSaveAddCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        String customerName = name.getText();
        String customerAddress = address.getText();
        String postal = postalCode.getText();
        String customerPhone = phoneNumber.getText();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = currentUser.getUsername();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdateBy = currentUser.getUsername();
        String division = String.valueOf(stateComboBox.getSelectionModel().getSelectedItem());
        int divisionId = Queries.getDivisionId(division);

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime localCreateTime = createDate.atZone(systemZone);
        ZonedDateTime utcCreateTime = localCreateTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime finalUtcCreateTime = utcCreateTime.plusHours(1);

        ZonedDateTime localUpdateTime = lastUpdate.atZone(systemZone);
        ZonedDateTime utcUpdateTime = localUpdateTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime finalUtcUpdateTime = utcUpdateTime.plusHours(1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = finalUtcCreateTime.format(dtf);
        String updateTime = finalUtcUpdateTime.format(dtf);


        Queries.createCustomer(customerName, customerAddress, postal, customerPhone, createTime,
                createdBy, updateTime, lastUpdateBy, divisionId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();

        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        stage2.setTitle("CRM Customers");
        stage2.setScene(new Scene(root, 1500, 800));
        stage2.show();


        JDBC.closeConnection();
    }

    /** Upon clicking cancel, return to the all customers screen. **/
    public void onCancelAddCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();

        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);

        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        stage2.setTitle("CRM Customers");
        stage2.setScene(new Scene(root, 1500, 800));
        stage2.show();
    }
}
