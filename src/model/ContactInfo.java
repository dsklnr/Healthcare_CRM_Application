package model;

/** Creating the contact class **/
public class ContactInfo {
    private int contactId;
    private String name;
    private String email;

    /** Creating the contact class constructor. **/
    public ContactInfo(int contactId, String name, String email) {
        this.contactId = contactId;
        this.name = name;
        this.email = email;
    }

    /** Get the contact ID.
     *
     * @return Returns the contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /** Set the contact ID. **/
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /** Get the contact name.
     *
     * @return Returns the contact name.
     */
    public String getName() {
        return name;
    }

    /** Set the contact name. **/
    public void setName(String name) {
        this.name = name;
    }

    /** Get the contact email address.
     *
     * @return Returns the contact email address.
     */
    public String getEmail() {
        return email;
    }

    /** Set the contact email address. **/
    public void setEmail(String email) {
        this.email = email;
    }

    /** Displays the contact name as a string.
     *
     * @return Returns the contact name as a string.
     */
    @Override
    public String toString(){
        return name;
    }
}
