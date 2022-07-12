package model;

public class Contact {
    private int contact_id;
    private String contact_name;
    private String email;

    /** Constructor of the Contact Class
     * @param contact_id id of the contact
     * @param contact_name contact's name
     * @param email contact's email
     */
    public Contact(int contact_id, String contact_name, String email){
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.email = email;

    }

    /**
     * Gets the contact's id
     * @return contact_id
     */
    public int getContact_id() {
        return contact_id;
    }

    /**
     * Sets the conact's id
     * @param contact_id
     */
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    /**
     * Gets the contact's name
     * @return contact_name
     */
    public String getContact_name() {
        return contact_name;
    }

    /**
     * Sets the contact's name
     * @param contact_name
     */
    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    /**
     * Gets the contact's email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the contact's email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    /**
     * used to display contact's name in a string format
     */
    public String toString(){
        return contact_name;
    }
}
