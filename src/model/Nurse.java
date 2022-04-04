package model;

/** Creating the Nurse class **/
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

    /** Get the nurse type.
     *
     * @return Returns the nurse type.
     */
    public String getNurseType() {
        return nurseType;
    }

    /** Set the nurse type.
     *
     * @param nurseType The nurse type.
     */
    public void setNurseType(String nurseType) {
        this.nurseType = nurseType;
    }
}
