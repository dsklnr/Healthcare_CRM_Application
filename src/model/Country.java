package model;

/** Creating the country class **/
public class Country {
    private int countryId;
    private String country;

    /** Creating the country class constructor. **/
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /** Get the country ID.
     *
     * @return Returns the country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /** Set the country ID. **/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** Get the country name.
     *
     * @return Returns the country name.
     */
    public String getCountry() {
        return country;
    }

    /** Set the country name. **/
    public void setCountry(String country) {
        this.country = country;
    }

    /** Displays the country name as a string.
     *
     * @return Returns the country name as a string.
     */
    @Override
    public String toString(){
        return country;
    }
}
