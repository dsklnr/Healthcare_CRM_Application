package model;

/** Creating the appointment class **/
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startDateTime;
    private String endDateTime;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    /** Creating the appointment class constructor. **/
    public Appointment(int appointmentId, String title, String description, String location, String type,
                       String startDateTime, String endDateTime, String createDate, String createdBy,
                       String lastUpdate, String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /** Get the appointment ID.
     *
     * @return Returns the appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /** Set the appointment ID. **/
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** Get the appointment title.
     *
     * @return Returns the appointment title.
     */
    public String getTitle() {
        return title;
    }

    /** Set the appointment title. **/
    public void setTitle(String title) {
        this.title = title;
    }

    /** Get the appointment description.
     *
     * @return Returns the appointment description.
     */
    public String getDescription() {
        return description;
    }

    /** Set the appointment description. **/
    public void setDescription(String description) {
        this.description = description;
    }

    /** Get the appointment location.
     *
     * @return Returns the appointment location.
     */
    public String getLocation() {
        return location;
    }

    /** Set the appointment location. **/
    public void setLocation(String location) {
        this.location = location;
    }

    /** Get the appointment type.
     *
     * @return Returns the appointment type.
     */
    public String getType() {
        return type;
    }

    /** Set the appointment type. **/
    public void setType(String type) {
        this.type = type;
    }

    /** Get the appointment start time & date.
     *
     * @return Returns the appointment start time & date.
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /** Set the appointment start time & date. **/
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** Get the appointment end time & date.
     *
     * @return Returns the appointment end time & date.
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /** Set the appointment end time & date. **/
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /** Get the appointment create date.
     *
     * @return Returns the appointment create date.
     */
    public String getCreateDate() {
        return createDate;
    }

    /** Set the appointment create date. **/
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /** Get the appointment created by field.
     *
     * @return Returns the appointment created by field.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Set the appointment created by field. **/
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Get the appointment last update.
     *
     * @return Returns the appointment last update.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /** Set the appointment last update date & time. **/
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Get the appointment last updated by field.
     *
     * @return Returns the appointment last updated by field.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Set the appointment last updated by field. **/
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Get the appointment customer ID.
     *
     * @return Returns the appointment customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /** Set the appointment customer ID. **/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** Get the appointment user ID.
     *
     * @return Returns the appointment user ID.
     */
    public int getUserId() {
        return userId;
    }

    /** Set the appointment user ID. **/
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** Get the appointment contact ID.
     *
     * @return Returns the appointment contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /** Set the appointment contact ID. **/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
