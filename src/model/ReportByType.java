package model;

public class ReportByType {
    public String apptType;
    public int apptTotal;

    /**
     * Constructor for the ReportByType class
     * @param apptType type of appointment
     * @param apptTotal number of appointments of that type in total
     */
    public ReportByType(String apptType, int apptTotal){
        this.apptType = apptType;
        this.apptTotal = apptTotal;
    }

    /**
     * Gets the apppointment's type
     * @return apptType
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * Gets the total number of appointment of that type
     * @return apptTotal
     */
    public int getApptTotal() {
        return apptTotal;
    }
}
