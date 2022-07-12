package model;

import java.time.LocalDateTime;

public class Appointment {

    private int appointment_id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start; // combine with local date and local time
    private LocalDateTime end;
    private int customer_id;
    private int user_id;
    private int contact_id;

   /** Constructor for the Appointment class
    * @param appointment_id id of an appointment
    * @param title title of a appointment
    * @param description description of an appointment
    * @param location location of the appointment
    * @param type type of appointment
    * @param start start date and time of an appointment
    * @param end end date and time of an appointment
    * @param customer_id id of a customer
    * @param user_id id if a user
    * @param contact_id id of a contact
    */
    public Appointment ( int appointment_id, String title, String description, String location, String type,
                         LocalDateTime start, LocalDateTime end, int customer_id, int user_id, int contact_id){

        this.appointment_id = appointment_id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.contact_id = contact_id;

    }

    /** Gets the appointment id
        @return appointment_id
     */
    public int getAppointment_id() {
        return appointment_id;
    }
    /** Sets the appointment id
     @param  appointment_id
     */
    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    /** Gets the appointment's title
     * @return title
     */
    public String getTitle() {
        return title;
    }
    /** Sets the appointment's title
     * @param  title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /** Gets the appointment's description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /** Sets the appointment's description
     * @param  description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /** Gets the appointment's location
     * @return location
     */
    public String getLocation() {
        return location;
    }
    /** Sets the appointment's location
     * @param  location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /** Gets the appointment's type
     * @return   type
     */
    public String getType() {
        return type;
    }
    /** Sets the appointment's type
     * @param    type
     */
    public void setType(String type) {
        this.type = type;
    }
    /** Gets the appointment's start date/time information
     * @return  start
     */
    public LocalDateTime getStart() {
        return start;
    }
    /** Sets the appointment's start date/time information
     * @param   start
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    /** Gets the appointment's start date/time information
     * @return end
     */
    public LocalDateTime getEnd() {
        return end;
    }
    /** Sets the appointment's start date/time information
     * @param  end
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    /** Gets the customer's id
     * @return  customer_id
     */
    public int getCustomer_id() {
        return customer_id;
    }
    /** Sets the customer's id
     * @param  customer_id
     */
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
    /** Gets the user's id
     * @return   user_id
     */
    public int getUser_id() {
        return user_id;
    }
    /** Sets the user's id
     * @param    user_id
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    /** Gets the contact's id
     * @return  contact_id
     */
    public int getContact_id() {
        return contact_id;
    }
    /** Sets the contact's id
     * @param   contact_id
     */
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }
}
