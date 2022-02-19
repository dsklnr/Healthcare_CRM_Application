package controller;

import dao.JDBC;
import dao.Queries;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.util.StringConverter;
import model.Country;
import model.Customer;
import model.Division;
import model.User;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {
    public ComboBox<Country> userComboBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

        ObservableList<Country> allCountries = Queries.getAllCountries();

        System.out.println(allCountries);
        userComboBox.setItems(allCountries);
        userComboBox.setPromptText("Select A Country");
        
        
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
