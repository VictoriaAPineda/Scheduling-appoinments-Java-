package model;

public class User {

    private Integer user_id;
    private String user_name;
    private String password;

    /**
     * Constructor for the User class
     * @param user_id id of the user
     * @param user_name name of the user
     * @param password password of the user
     */
    public User(Integer user_id, String user_name, String password){
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;

    }

    /**
     * Gets the user id
     * @return user_id
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * Sets the user_id
     * @param user_id
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the user's name
     * @return user_name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Sets the user's name
     * @param user_name
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Gets the user's password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    /**
     *  used to display user's name in a string format
     */
    public String toString(){
        return user_name;
    }
}
