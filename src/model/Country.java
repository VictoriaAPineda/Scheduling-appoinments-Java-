package model;

public class Country {

    private int country_id;
    private String country_Name;

    /**
     * Construcor for the Country class
     * @param country_id id of the country
     * @param country_Name name of the country
     */
    public Country(int country_id, String country_Name){
        this.country_id = country_id;
        this.country_Name = country_Name;

    }

    /**
     * Gets the country's id
     * @return country_id
     */
    public int getCountry_id() {
        return country_id;
    }

    /**
     * Set's the country's id
     * @param country_id
     */
    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    /**
     * Gets the country's name
     * @return
     */
    public String getCountry_Name() {
        return country_Name;
    }

    /**
     * Sets the coutry's name
     * @param country_Name
     */
    public void setCountry_Name(String country_Name) {
        this.country_Name = country_Name;
    }
}
