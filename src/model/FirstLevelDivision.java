package model;

public class FirstLevelDivision {
    private int division_id;
    private String division_name;
    private int country_id;

    /**
     * Constructor for the FirstLevelDivision class
     * @param division_id id of the division
     * @param division_name name of the division
     * @param country_id country that the division belongs to
     */
    public FirstLevelDivision (int division_id, String division_name, int country_id){
        this.division_id = division_id;
        this.division_name = division_name;
        this.country_id = country_id;
    }

    /**
     * Get the division id
     * @return division_id
     */
    public int getDivision_id() {
        return division_id;
    }

    /**
     * Sets the division_id
     * @param division_id
     */
    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }

    /**
     * Gets the name of the division
     * @return division_name
     */
    public String getDivision_name() {
        return division_name;
    }

    /**
     * Sets the  name of the division
     * @param division_name
     */
    public void setDivision_name(String division_name) {
        this.division_name = division_name;
    }

    /**
     * Gets the country id
     * @return country_id
     */
    public int getCountry_id() {
        return country_id;
    }

    /**
     * Sets the country's id
     * @param country_id
     */
    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }
}
