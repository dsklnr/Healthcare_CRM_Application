package main;

import com.sun.javafx.binding.StringFormatter;
import dao.JDBC;
import dao.Queries;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("User Login");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();

        /*
        LocalTime startTime = LocalTime.of(10,32);
        LocalTime endTime = LocalTime.of(10, 45);
        LocalTime currentTime = LocalTime.now();
        long timeDifference = ChronoUnit.MINUTES.between(startTime, currentTime);

        //System.out.println((timeDifference + -1) * -1);

        long interval = ((timeDifference + -1) * -1);
        //long interval = timeDifference;

        if (interval > 0 && interval <= 10){
            System.out.println("You have an event in approximately " + interval + " minute(s)");
        }

        else if (interval <= 1){
            System.out.println("Event started " + interval * -1 + " minute(s) ago");
        }

       */
        JDBC.openConnection();


        ObservableList<LocalDateTime> appointmentStartDateTimes = FXCollections.observableArrayList();
        ObservableList<LocalDateTime> appointmentEndDateTimes = FXCollections.observableArrayList();

        appointmentStartDateTimes = Queries.getAllStartTimes();
        appointmentEndDateTimes = Queries.getAllEndTimes();
        //System.out.println(appointmentStartDateTimes);

        ObservableList<LocalDateTime> startTimes = FXCollections.observableArrayList();
        ObservableList<LocalDateTime> endTimes = FXCollections.observableArrayList();

        for (LocalDateTime x : appointmentStartDateTimes) {
            int startYear = x.getYear();
            Month startMonth = x.getMonth();
            int startDay = x.getDayOfMonth();
            int startHour = x.getHour();
            int startMinute = x.getMinute();

            LocalDateTime startLDT = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
            startTimes.addAll(startLDT);
        }

        for (LocalDateTime y : appointmentEndDateTimes){
            int endYear = y.getYear();
            Month endMonth = y.getMonth();
            int endDay = y.getDayOfMonth();
            int endHour = y.getHour();
            int endMinute = y.getMinute();

            LocalDateTime endLDT = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
            endTimes.addAll(endLDT);
        }

        DateTimeFormatter dateTF = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");

        for (int i = 0; i < startTimes.size(); i++){
            LocalDateTime currentStart = startTimes.get(i);
            LocalDateTime currentEnd = endTimes.get(i);
            LocalDateTime myDT = LocalDateTime.of(2020, 05, 28, 12, 00);

            if (myDT.isAfter(currentStart) && myDT.isBefore(currentEnd)){
                System.out.println("Your date and time overlaps another appointment starting at " +
                        currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
            }

            if (myDT.equals(currentStart) || myDT.equals(currentEnd)){
                System.out.println("Your date and time overlaps another appointment starting at " +
                        currentStart.format(dateTF) + " and ending at " + currentEnd.format(dateTF));
            }

        }

        /*
        LocalDateTime startDT = LocalDateTime.of(2022, 02, 25, 10, 00);
        LocalDateTime endDT = LocalDateTime.of(2022, 02, 25, 10, 30);
        LocalDateTime myDT = LocalDateTime.of(2022, 02, 25, 10, 10);

        if (myDT.isAfter(startDT) && myDT.isBefore(endDT)){
            System.out.println(myDT + " Your date time overlaps " + startDT + " and " + endDT);
        }

        else if (myDT.equals(startDT) || myDT.equals(endDT)){
            System.out.println("Your date time matches " + startDT + " or " + endDT);
        }

        else {
            System.out.println("Your date time does not overlap");
        }

         */





        JDBC.closeConnection();
    }

    public static void main(String[] args){
        launch(args);
    }
}
