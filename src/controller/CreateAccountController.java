package controller;

import dao.CreateAccountQueries;
import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Doctor;
import model.Nurse;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Creating the create account controller. **/
public class CreateAccountController implements Initializable {
    public TextField fullName;
    public TextField email;
    public TextField setUsername;
    public PasswordField setPassword;
    public Label comboBoxTitle;
    public ChoiceBox<String> levels;
    public RadioButton doctorButton;
    public ToggleGroup employeeType;
    public RadioButton nurseButton;
    public ObservableList<String> doctorLevel = FXCollections.observableArrayList();
    public ObservableList<String> nurseType = FXCollections.observableArrayList();

    /** Initialize the create account controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        doctorLevel.add("Intern");
        doctorLevel.add("Fellow");
        doctorLevel.add("Junior Resident");
        doctorLevel.add("Senior Resident");
        doctorLevel.add("Chief Resident");
        doctorLevel.add("Attending Physician");
        doctorLevel.add("Head of Department");
        doctorLevel.add("Medical Director");

        nurseType.add("CNA");
        nurseType.add("LPN");
        nurseType.add("RN");
        nurseType.add("ER RN");
        nurseType.add("Surgical Assistant RN");
        nurseType.add("Labor/Delivery Nurse");

        doctorButton.setSelected(true);
        levels.getItems().setAll(doctorLevel);

    }

    /** Insert a new user into the database.
     *
     * A lambda is used to encrypt user passwords before they are inserted into the database.
     * **/
    public void onCreateAccount(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        String name = fullName.getText();
        String contactEmail = email.getText();
        String user = setUsername.getText();
        String password = setPassword.getText();

        if (doctorButton.isSelected()){
            char[] userPassword = password.toCharArray();

            ObservableList<String> encryptedPassword = FXCollections.observableArrayList();

            for (char pass : userPassword){
                pass += 10;
                encryptedPassword.add(String.valueOf(pass));
            }

            String lvl = levels.getSelectionModel().getSelectedItem();

            CreateAccountQueries.insertDoctor(user, String.valueOf(encryptedPassword), lvl);
            CreateAccountQueries.insertContact(name, contactEmail);
        }

        if (nurseButton.isSelected()){
            char[] userPassword = password.toCharArray();

            ObservableList<String> encryptedPassword = FXCollections.observableArrayList();

            for (char pass : userPassword){
                pass += 10;
                encryptedPassword.add(String.valueOf(pass));
            }

            String type = levels.getSelectionModel().getSelectedItem();

            CreateAccountQueries.insertNurse(user, String.valueOf(encryptedPassword), type);
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        JDBC.closeConnection();

    }

    public void onDoctorButton(ActionEvent actionEvent) {
        doctorButton.setSelected(true);
        levels.getItems().setAll(doctorLevel);
    }

    public void onNurseButton(ActionEvent actionEvent) {
        nurseButton.setSelected(true);
        levels.getItems().setAll(nurseType);
    }
}
