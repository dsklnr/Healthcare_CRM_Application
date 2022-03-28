package model;

public class Doctor extends User{
    private int doctorId;
    private String doctorLevel;

    /**
     * Creating the user class constructor.
     *
     * @param userId
     * @param username
     * @param password
     **/
    public Doctor(int doctorId, int userId, String username, String password, String doctorLevel) {
        super(userId, username, password);

        this.doctorId = doctorId;
        this.doctorLevel = doctorLevel;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }
}
