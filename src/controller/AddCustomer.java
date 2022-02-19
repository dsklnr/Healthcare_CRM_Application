package controller;

import dao.JDBC;
import dao.Queries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import model.Country;
import model.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> stateComboBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Country> allCountries = Queries.getAllCountries();
        ObservableList<Division> allDivisions = Queries.getAllDivisions();

        countryComboBox.setItems(allCountries);
        countryComboBox.setPromptText("Select A Country");
        stateComboBox.setItems(allDivisions);
        stateComboBox.setPromptText("Select State or Province");
        
        
        JDBC.closeConnection();
    }


    public String toString(){
        //ObservableList<Customer> customers = Queries.getAllCustomers();
        return (getClass().getName());
        //return (getClass().getName().toString());

    }



    public void onSaveAddCustomer(ActionEvent actionEvent) {
    }

    public void onCancelAddCustomer(ActionEvent actionEvent) {
    }
}
