package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Patient;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Creating the all customers controller. **/
public class AllPatientsController implements Initializable {
    public TableColumn patientIdCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneNumberCol;
    public TableColumn createDateCol;
    public TableColumn createdByCol;
    public TableColumn lastUpdateCol;
    public TableColumn lastUpdatedByCol;
    public TableColumn divisionIdCol;
    public TableView patientsTable;
    public User currentUser;
    public TextField searchPatients;
    public Label homeLabel;
    public Label patientsLabel;
    public Label scheduleLabel;
    public Label reportLabel;
    public ImageView logo;
    public TableColumn CountryCol;

    /** Initialize the all patients controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/Brackets_White.png");
        logo.setImage(image);
        
        homeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        patientsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        scheduleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        reportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        //createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        //createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        //lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        //lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        patientsTable.setItems(Queries.getAllPatients());

        JDBC.closeConnection();
    }

    /** Set the user object to the currently logged-in user. **/
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /** Upon clicking home, go to the dashboard screen. **/
    public void onHomeClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardScreen.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("CRM Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking patients, go to the all patients screen. **/
    public void onPatientsClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllPatientsScreen.fxml"));
        Parent root = loader.load();
        AllPatientsController allPatientsController = loader.getController();
        allPatientsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Patients");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController allAppointmentsController = loader.getController();
        allAppointmentsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(currentUser);
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add("/css/styles.css");
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /** Upon clicking the add button, go to the add patient screen. **/
    public void onAddPatient(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPatientScreen.fxml"));
        Parent root = loader.load();
        AddPatientController addPatientController = loader.getController();
        addPatientController.setUser(currentUser);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Image image = new Image("/icons/Brackets_Black.png");
        stage.getIcons().add(image);
        stage.setTitle("Add Patient");
        stage.setScene(new Scene(root, 500, 600));
        stage.show();
    }

    /** Upon clicking the update button, go to the add appointment screen and populate the form based on the patient
     * selected. **/
    public void onUpdatePatient(ActionEvent actionEvent) throws IOException {
        Patient patient = (Patient) patientsTable.getSelectionModel().getSelectedItem();

        if (patient != null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdatePatientScreen.fxml"));
                Parent root = loader.load();
                UpdatePatientController updatePatientController = loader.getController();
                updatePatientController.setPatient(patient);
                UpdatePatientController user = loader.getController();
                user.setUser(currentUser);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
                Image image = new Image("/icons/Brackets_Black.png");
                stage.getIcons().add(image);
                stage.setTitle("Update Patient");
                stage.setScene(new Scene(root, 500, 600));
                stage.show();

            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /** Upon clicking the delete button, delete the selected patient if they do not have any appointment(s). **/
    public void onDeletePatient(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        Patient patient = (Patient) patientsTable.getSelectionModel().getSelectedItem();

        if (patient != null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image image = new Image("/icons/Confirmation.png");
            stage.getIcons().add(image);
            alert.setTitle("Delete Patient");
            alert.setContentText("Are you sure you want to delete this patient?");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get().equals(ButtonType.OK) && Queries.checkDeletePatient(patient.getPatientID())) {
                Queries.deletePatient(patient.getPatientID());
                alert.close();
            }

            if (action.get().equals(ButtonType.OK) && !Queries.checkDeletePatient(patient.getPatientID())){
                alert.close();
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                Stage stage2 = (Stage) alert2.getDialogPane().getScene().getWindow();
                Image image2 = new Image("/icons/Error.png");
                stage2.getIcons().add(image2);
                alert2.setTitle("Error");
                alert2.setContentText("Must delete all appointments associated with this patient before deleting the patient record");
                alert2.showAndWait();
            }

            patientsTable.setItems(Queries.getAllPatients());

        }

        JDBC.closeConnection();
    }

    /** Upon typing in the search bar, display the searched patient name. **/
    public void onSearchPatients(ActionEvent actionEvent) {
        JDBC.openConnection();

        String search = searchPatients.getText();

        ObservableList<Patient> allPatients = Queries.getAllPatients();
        ObservableList<Patient> patientsSearch = FXCollections.observableArrayList();

        for (Patient c : allPatients){
            if (c.getName().contains(search)){
                patientsSearch.addAll(c);
            }

        }


        patientsTable.setItems(patientsSearch);

        JDBC.closeConnection();;
    }
}
