package model;
// will hold data for the 3rd report
public class ReportCountry {

    private int countCountry;
    private String countryName;

    /**
     * Constructor of teh ReportCountry
     * @param countryName name of country
     * @param countCountry number of customers in country
     */
    public ReportCountry(String countryName, int countCountry){
        this.countryName = countryName;
        this.countCountry = countCountry;
    }

    /**
     * Gets the number of customers within that country
     * @return countCountry
     */
    public int getCountCountry() {
        return countCountry;
    }

    /**
     * Gets teh country's name
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }
}



