package model;

public class ReportByMonth {

    public  String apptMonth;
    public int apptTotal;

    /**
     * Constructor for the REportByMonth class
     * @param apptMonth month of appointment
     * @param apptTotal number of appointments made in that month
     */
    public ReportByMonth(String apptMonth, int apptTotal){
        this.apptMonth = apptMonth;
        this.apptTotal = apptTotal;
    }

    /**
     * Gets month of appointment
     * @return apptsMonth
     */
    public String getApptMonth() {
        return apptMonth;
    }

    /**
     * Gets the total number of appointments
     * @return apptTotal
     */
    public int getApptTotal() {
        return apptTotal;
    }
}
