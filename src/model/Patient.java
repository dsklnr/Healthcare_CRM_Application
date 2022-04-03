package model;

/** Creating the customer class **/
public class Patient {
    private int patientID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;
    private int divisionId;

    /** Creating the customer class constructor. **/
    public Patient(int patientId, String name, String address, String postalCode, String phoneNumber, String createDate, String createdBy, String lastUpdate, String lastUpdateBy, int divisionId) {
        this.patientID = patientId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.divisionId = divisionId;
    }

    /** Get the customer ID.
     *
     * @return Returns the customer ID.
     */
    public int getPatientID() {
        return patientID;
    }

    /** Set the customer ID. **/
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /** Get the customer name.
     *
     * @return Returns the customer name.
     */
    public String getName() {
        return name;
    }

    /** Set the customer name. **/
    public void setName(String name) {
        this.name = name;
    }

    /** Get the customer address.
     *
     * @return Returns the customer address.
     */
    public String getAddress() {
        return address;
    }

    /** Set the customer address. **/
    public void setAddress(String address) {
        this.address = address;
    }

    /** Get the customer postal code.
     *
     * @return Returns the customer postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** Set the customer postal code. **/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Get the customer phone number.
     *
     * @return Returns the customer phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Set the customer phone number. **/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** Get the customer create date.
     *
     * @return Returns the customer create date.
     */
    public String getCreateDate() {
        return createDate;
    }

    /** Set the customer create date. **/
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /** Get the customer created by field.
     *
     * @return Returns the customer created by field.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Set the customer created by field. **/
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Get the customer last update date & time.
     *
     * @return Returns the customer last update date & time.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /** Set the customer last update date & time. **/
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Get the customer last updated by field.
     *
     * @return Returns the customer last updated by field.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** Set the customer last updated by field. **/
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** Get the customer division ID.
     *
     * @return Returns the customer division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Set the customer division ID. **/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
