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
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Creating the all customers controller. **/
public class AllCustomersController implements Initializable {
    public TableColumn customerIdCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneNumberCol;
    public TableColumn createDateCol;
    public TableColumn createdByCol;
    public TableColumn lastUpdateCol;
    public TableColumn lastUpdatedByCol;
    public TableColumn divisionIdCol;
    public TableView customersTable;
    public User currentUser;
    public TextField searchCustomers;
    public Label homeLabel;
    public Label customersLabel;
    public Label scheduleLabel;
    public Label reportLabel;
    public ImageView logo;
    public TableColumn CountryCol;

    /** Initialize the all customers controller. **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        Image image = new Image("/icons/Brackets.png");
        logo.setImage(image);
        
        homeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        customersLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        scheduleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        reportLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        //createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        //createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        //lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        //lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        customersTable.setItems(Queries.getAllCustomers());

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
        DashboardController dashboardUser = loader.getController();
        dashboardUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking customers, go to the all customers screen. **/
    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllCustomersScreen.fxml"));
        Parent root = loader.load();
        AllCustomersController customerUser = loader.getController();
        customerUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking schedule appointment, go to the all appointments screen. **/
    public void onScheduleClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        Parent root = loader.load();
        AllAppointmentsController appointmentsUser = loader.getController();
        appointmentsUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Dashboard");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking generate reports, go to the reports screen. **/
    public void onReportClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        Parent root = loader.load();
        ReportsController reportsController = loader.getController();
        reportsController.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Reports");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

    /** Upon clicking the add button, go to the add customer screen. **/
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomerScreen.fxml"));
        Parent root = loader.load();
        AddCustomerController customerUser = loader.getController();
        customerUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("Add A Customer");
        stage.setScene(new Scene(root, 500, 600));
        stage.show();
    }

    /** Upon clicking the update button, go to the add appointment screen and populate the form based on the customer
     * selected. **/
    public void onUpdateCustomer(ActionEvent actionEvent) throws IOException {
        Customer customer = (Customer) customersTable.getSelectionModel().getSelectedItem();

        if (customer != null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomerScreen.fxml"));
                Parent root = loader.load();
                UpdateCustomer selectedCustomer = loader.getController();
                selectedCustomer.setCustomer(customer);
                UpdateCustomer customerUser = loader.getController();
                customerUser.setUser(currentUser);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
                stage.setTitle("Add A Customer");
                stage.setScene(new Scene(root, 500, 600));
                stage.show();

            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /** Upon clicking the delete button, delete the selected customer if they do not have any appointment(s). **/
    public void onDeleteCustomer(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();

        Customer customer = (Customer) customersTable.getSelectionModel().getSelectedItem();

        if (customer != null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get().equals(ButtonType.OK) && Queries.checkDeleteCustomer(customer.getCustomerId())) {
                Queries.deleteCustomer(customer.getCustomerId());
                alert.close();
            }

            if (action.get().equals(ButtonType.OK) && !Queries.checkDeleteCustomer(customer.getCustomerId())){
                alert.close();
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error");
                alert2.setContentText("Must delete appointments associated with customers before deleting customers");
                alert2.showAndWait();
            }

            customersTable.setItems(Queries.getAllCustomers());

        }

        JDBC.closeConnection();
    }

    /** Upon typing in the search bar, display the searched customer name. **/
    public void onSearchCustomers(ActionEvent actionEvent) {
        JDBC.openConnection();

        String search = searchCustomers.getText();

        ObservableList<Customer> allCustomers = Queries.getAllCustomers();
        ObservableList<Customer> customerSearch = FXCollections.observableArrayList();

        for (Customer c : allCustomers){
            if (c.getName().contains(search)){
                customerSearch.addAll(c);
            }

        }


        customersTable.setItems(customerSearch);

        JDBC.closeConnection();;
    }
}
