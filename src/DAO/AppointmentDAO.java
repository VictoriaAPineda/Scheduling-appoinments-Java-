package DAO;

import DBConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Class deals with appointment data
 */
public class AppointmentDAO {
    // a list to hold all types of appointments
    private static ObservableList<String> allApptTypes = FXCollections.observableArrayList();

    /**
     * This method gets information about an appointment
     * @return observable list of appointments
     */
    public static ObservableList<Appointment> getAppointments (){
        // selecting which columns to pull data from
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID," +
                "User_ID, Contact_ID FROM appointments";
        // a list to hold the data
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Appointment a = new Appointment(rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                allAppointments.add(a);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return allAppointments;
    }

    /**
     * Method to add an appointment
     * @param title title of an appointment
     * @param description appointment description
     * @param location appointment location
     * @param apptType the type of an appoinment
     * @param contact_Name name of contact
     * @param start Date/Time start of an appointment
     * @param end Date/Time end of an appointment
     * @param customer_ID id of customer
     * @param user_ID user id
     * @return true if appointment is inserted in sql table correctly
     * @throws SQLException returns false and stack trace. Was not inserted into sql table
     */
    public static boolean addAppointment (String title, String description, String location,
                                      String apptType, String contact_Name, LocalDateTime start, LocalDateTime end, Integer customer_ID,
                                      Integer user_ID) throws SQLException {

    String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
    // create auto appt id
    Integer apptGenID = (int) Math.random()* 1000;
    // sets the inputted data from the form into table
    ps.setInt(1,apptGenID);
    ps.setString(2,title);
    ps.setString(3,description);
    ps.setString(4,location);
    ps.setString(5,apptType);
    ps.setTimestamp(6, Timestamp.valueOf(start));
    ps.setTimestamp(7,Timestamp.valueOf(end));
    ps.setInt(8,customer_ID);
    ps.setInt(9,user_ID);
    ps.setString(10,contact_Name);

    try {
        ps.execute();
        return true;
    }catch (Exception ex){
        ex.printStackTrace();
        return false;
    }

}
    /**
     * Method changes and appointment's data
     * @param title title of appointment
     * @param description appointment description
     * @param location appointment location
     * @param apptType type of appointment
     * @param contact_Name name of contact
     * @param start Date/Time start of appointment
     * @param end Date/Time end of appointment
     * @param customer_ID id of customer
     * @param user_ID id of user
     * @param apptID appointment id, used to determine which appointment to change
     * @return true, appointment has been changed
     * @throws SQLException returns false and throw SQLException if was unable to change
     */
    public static boolean updateAppointment (String title, String description, String location,
                                         String apptType, String contact_Name, LocalDateTime start, LocalDateTime end, Integer customer_ID,
                                         Integer user_ID,Integer apptID) throws SQLException {

        String sql = "UPDATE appointments SET Title=?, Description=?,Location=?,Type=?,Contact_ID=?,Start=?,End=?, Customer_ID=?,User_ID=? WHERE Appointment_ID =?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1,title);
        ps.setString(2,description);
        ps.setString(3,location);
        ps.setString(4,apptType);
        ps.setString(5,contact_Name);
        ps.setTimestamp(6,Timestamp.valueOf(start));
        ps.setTimestamp(7,Timestamp.valueOf(end));
        ps.setInt(8,customer_ID);
        ps.setInt(9,user_ID);
        ps.setInt(10,apptID);

        try{
            ps.execute();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
}

    /**
     * Method to get list of appointments for a customer by their customer ID
     * @param custID id of customer
     * @return Observable list of all appointments that the customer has
     */
    public static ObservableList<Appointment> getApptsByCustomerID (int custID) {
        // list to hold the appointments
        ObservableList<Appointment> appts = FXCollections.observableArrayList();
        // the ? is where the parameter goes into the SQL code
        String sql = "SELECT * FROM appointments WHERE Customer_ID= ?";

        try {
            // connects to sql database
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, custID);// takes the input from parameter to be used in query
            ResultSet rs = ps.executeQuery(); // run the query

            while(rs.next()){
                Appointment newAppt =  new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")
                );
                appts.add(newAppt);
            }

            return appts;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Method deletes an appoinment based on the appointment ID
     * @param apptID appointment id
     * @return true if deleted, false if error/not deleted
     */
    public  static boolean deleteAppointment (int apptID){
        String sql = "DELETE from appointments WHERE Appointment_ID=?";

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1,apptID);
            ps.execute();
            return true;

        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Method gets all the named types of appointments from a appointments observable list
     * @return Observable list of the each of the appointment types in a list
     */
    public  static ObservableList<String> getAllApptTypes (){
        if(allApptTypes.isEmpty()) {
            ObservableList<Appointment> allTypes = AppointmentDAO.getAppointments();

            for (Appointment appt : allTypes) {
                if (allApptTypes.contains(appt.getType())) {
                    continue;
                }
                allApptTypes.add(appt.getType());
            }
            // hard coded list of appointment types r user to choose from
            if (!allApptTypes.contains("Consultation")) {// will avoid repeating same types
                allApptTypes.add("Consultation");
            }
            if (!allApptTypes.contains("Planning")) {
                allApptTypes.add("Planning");
            }
            if (!allApptTypes.contains("Progression Check")) {
                allApptTypes.add("Progression Check");
            }
            if (!allApptTypes.contains("Ending Consultation aid")) {
                allApptTypes.add("Ending Consultation aid");
            }

        }

        return allApptTypes;
    }


}
