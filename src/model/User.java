package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Creating the user class **/
public class User {
    private int userId;
    private String username;
    private String password;

    /** Creating the user class constructor. **/
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /** Get the user's ID.
     *
     * @return Returns the user's ID.
     */
    public int getUserId() {
        return userId;
    }

    /** Set the user's ID. **/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Get the user's username.
     *
     * @return Returns the user's username.
     */
    public String getUsername() {
        return username;
    }

    /** Set the user's username. **/
    public void setUsername(String username) {
        this.username = username;
    }

    /** Get the user's password.
     *
     * @return Returns the user's password.
     */
    public String getPassword() {
        return password;
    }

    /** Set the user's password. **/
    public void setPassword(String password) {
        this.password = password;
    }

    public static ObservableList<Doctor> allDoctors = FXCollections.observableArrayList();

    public static ObservableList<Nurse> allNurses = FXCollections.observableArrayList();

    public static void addDoctor(Doctor doctor){
        allDoctors.add(doctor);
    }

    public static void addNurse(Nurse nurse){
        allNurses.add(nurse);
    }

    public static ObservableList<Doctor> getAllDoctors(){
        return allDoctors;
    }

    public static ObservableList<Nurse> getAllNurses(){
        return allNurses;
    }

}
