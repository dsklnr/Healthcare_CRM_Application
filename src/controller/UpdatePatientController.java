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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Country;
import model.Patient;
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

/** Creating the update patient controller. **/
public class UpdatePatientController implements Initializable {
    public User currentUser;
    public ComboBox countryComboBox;
    public ComboBox stateComboBox;
    public TextField patientId;
    public TextField name;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;
    public Patient selectedPatient;
    public LocalDateTime createDate;
    public String createdBy;
    public int countryId;

    /** Initialize the update patient controller. **/
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

    /** Populate the form with the selected patient's information. **/
    public void setPatient(Patient patient) throws SQLException {
        JDBC.openConnection();

        selectedPatient = patient;

        int id = patient.getPatientID();
        String name = patient.getName();
        String address = patient.getAddress();
        String postal = patient.getPostalCode();
        String phone = patient.getPhoneNumber();
        //String createdDate = patient.getCreateDate();
        createdBy = patient.getCreatedBy();

        int divisionId = patient.getDivisionId();

        String state = Queries.getDivisionName(divisionId);
        int countryId = Queries.getCountryId(divisionId);
        String country = Queries.getCountryName(divisionId, countryId);

        patientId.setText(String.valueOf(id));
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

    /** Update the patient in the MySQL database. **/
    public void onSaveUpdatePatient(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        int id = Integer.parseInt(patientId.getText());
        String patientName = name.getText();
        String patientAddress = address.getText();
        String postal = postalCode.getText();
        String patientPhone = phoneNumber.getText();

        String createdBy = currentUser.getUsername();
        String create = selectedPatient.getCreateDate();
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

        Queries.updatePatient(patientName, patientAddress, postal, patientPhone, create,
                createdBy, updateTime, lastUpdateBy, divisionId, id);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Patients");
        stage.setScene(scene);
        stage.show();

        JDBC.closeConnection();
    }

    /** Upon selecting cancel, return to the all patients screen. **/
    public void onCancelUpdatePatient(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Patients");
        stage.setScene(scene);
        stage.show();
    }
}
