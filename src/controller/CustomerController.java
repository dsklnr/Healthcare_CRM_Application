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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdateBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        customersTable.setItems(Queries.getAllCustomers());

        JDBC.closeConnection();
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
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

    public void onCustomersClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomersScreen.fxml"));
        Parent root = loader.load();
        CustomerController customerUser = loader.getController();
        customerUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("CRM Customers");
        stage.setScene(new Scene(root, 1500, 800));
        stage.show();
    }

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

    public void onReportClick(MouseEvent mouseEvent) {
    }

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomerScreen.fxml"));
        Parent root = loader.load();
        AddCustomer customerUser = loader.getController();
        customerUser.setUser(currentUser);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        stage.setTitle("Add A Customer");
        stage.setScene(new Scene(root, 500, 600));
        stage.show();
    }

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
                Stage stage = new Stage();
                stage.setTitle("Add A Customer");
                stage.setScene(new Scene(root, 500, 600));
                stage.show();

            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

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
