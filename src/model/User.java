package model;

/** Creating the user class **/
public class User {
    private int userId;
    private String username;
    private String password;
    private int doctorId;

    /** Creating the user class constructor. **/
    public User(int userId, String username, String password, int contactId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.doctorId = contactId;
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

    /** Get the doctor's ID.
     *
     * @return Returns the doctor's ID.
     */
    public int getDoctorId() {
        return doctorId;
    }

    /** Set the doctor's ID.
     *
     * @param doctorId The doctor's ID.
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /** Get the user's ID.
     *
     * @return Returns the user's ID.
     */
    @Override
    public String toString(){
        return String.valueOf(userId);
    }
}
