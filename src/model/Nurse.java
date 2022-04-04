package model;

public class Nurse extends User{
    private String nurseType;

    /**
     * Creating the user class constructor.
     *
     * @param userId
     * @param username
     * @param password
     **/
    public Nurse(int userId, String username, String password, String nurseType, int doctorId) {
        super(userId, username, password, doctorId);

        this.nurseType = nurseType;
    }

    public String getNurseType() {
        return nurseType;
    }

    public void setNurseType(String nurseType) {
        this.nurseType = nurseType;
    }
}
