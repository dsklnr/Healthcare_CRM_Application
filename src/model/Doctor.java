package model;

/** Creating the Doctor class **/
public class Doctor extends User{
    private String doctorLevel;

    /**
     * Creating the user class constructor.
     *
     * @param userId
     * @param username
     * @param password
     **/
    public Doctor(int userId, String username, String password, String doctorLevel, int doctorId) {
        super(userId, username, password, doctorId);

        this.doctorLevel = doctorLevel;
    }

    /** Get the doctor's level.
     *
     * @return Returns the doctor's level.
     */
    public String getDoctorLevel() {
        return doctorLevel;
    }

    /** Set the doctor's level.
     *
     * @param doctorLevel The doctor's level.
     */
    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }
}
