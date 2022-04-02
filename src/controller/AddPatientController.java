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

/** Creating the add patient controller. **/
public class AddPatientController implements Initializable {
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> stateComboBox;
    public TextField name;
    public TextField address;
    public TextField postalCode;
    public TextField phoneNumber;
    public User currentUser;
    public int countryId;

    /** Initialize the add patient controller. **/
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

    /** Insert a new patient into the database. **/
    public void onSaveAddPatient(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        String patientName = name.getText();
        String patientAddress = address.getText();
        String postal = postalCode.getText();
        String patientPhone = phoneNumber.getText();
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


        Queries.createPatient(patientName, patientAddress, postal, patientPhone, createTime,
                createdBy, updateTime, lastUpdateBy, divisionId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage2.getIcons().add(image);
        stage2.setTitle("Patients");
        stage2.setScene(scene);
        stage2.show();


        JDBC.closeConnection();
    }

    /** Upon clicking cancel, return to the all patients screen. **/
    public void onCancelAddPatient(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage2 = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage2.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage2.getIcons().add(image);
        stage2.setTitle("Patients");
        stage2.setScene(scene);
        stage2.show();
    }
}
