package model;

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

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }
}
