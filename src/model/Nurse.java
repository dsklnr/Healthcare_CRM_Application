package model;

public class Nurse extends User{
    private int nurseId;
    private String nurseType;

    /**
     * Creating the user class constructor.
     *
     * @param userId
     * @param username
     * @param password
     **/
    public Nurse(int nurseId, int userId, String username, String password, String nurseType) {
        super(userId, username, password);

        this.nurseId = nurseId;
        this.nurseType = nurseType;
    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseType() {
        return nurseType;
    }

    public void setNurseType(String nurseType) {
        this.nurseType = nurseType;
    }
}
