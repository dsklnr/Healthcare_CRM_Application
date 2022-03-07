package model;

/** Creating the division class **/
public class Division {
    private int divisionId;
    private String division;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;
    private int countryId;

    /** Creating the division class constructor. **/
    public Division(int divisionId, String division, String createDate, String createdBy, String lastUpdate, String lastUpdateBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.countryId = countryId;
    }

    /** Get the division ID.
     *
     * @return Returns the division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Set the division ID. **/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** Get the division name.
     *
     * @return Returns the division name.
     */
    public String getDivision() {
        return division;
    }

    /** Set the division name. **/
    public void setDivision(String division) {
        this.division = division;
    }

    /** Get the division create date.
     *
     * @return Returns the division create date.
     */
    public String getCreateDate() {
        return createDate;
    }

    /** Set the division create date. **/
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /** Get the division created by field.
     *
     * @return Returns the division created by field.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Set the division created by field. **/
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Get the division last update date & time.
     *
     * @return Returns the division last update date & time.
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /** Set the division last update date & time. **/
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Get the division last updated by field.
     *
     * @return Returns the division last updated by field.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** Set the division last update by field. **/
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** Get the division country ID.
     *
     * @return Returns the division country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /** Set the division country ID. **/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** Displays the division name as a string.
     *
     * @return Returns the division name as a string.
     */
    @Override
    public String toString(){
        return division;
    }
}
