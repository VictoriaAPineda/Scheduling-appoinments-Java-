package model;


public class Customer {

    private int customer_id;
    private String customer_name;
    private String address;
    private String postal_Code;
    private String phone;
    private int division_id;

    /**
     * Constructor for the customer class
     * @param customer_id id of customer
     * @param customer_name name of customer
     * @param address address of the customer
     * @param postal_Code postal code of the customer
     * @param phone phone numnber of the customer
     * @param division_id division id of where customer is located
     */
    public Customer (int customer_id, String customer_name, String address, String postal_Code,
                     String phone, int division_id){
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.address = address;
        this.postal_Code = postal_Code;
        this.phone = phone;
        this.division_id = division_id;

    }

    /**
     * Gets the customer's id
     * @return customer_id
     */
    public int getCustomer_id() {
        return customer_id;
    }

    /**
     * Sets the cusomter's id
     * @param customer_id
     */
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * Gets the customer's name
     * @return customer_name
     */
    public String getCustomer_name() {
        return customer_name;
    }

    /**
     * Sets the customer's name
     * @param customer_name
     */
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    /**
     * Gets the customer's address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the customer's address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the customer's postal code
     * @return postal_code
     */
    public String getPostal_Code() {
        return postal_Code;
    }

    /**
     * Sets the customer's postal code
     * @param postal_Code
     */
    public void setPostal_Code(String postal_Code) {
        this.postal_Code = postal_Code;
    }

    /**
     * Gets the customer's phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the customer's phone number
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the division id
     * @return division_id
     */
    public int getDivision_id() {
        return division_id;
    }

    /**
     * Sets the division id
     * @param division_id
     */
    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }

}
