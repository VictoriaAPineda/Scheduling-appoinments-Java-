package DAO;

import DBConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.FirstLevelDivision;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class works with data from a customer
 */
public class CustomerDAO  {
    /**
     * Method gets all customers
     * @return Observable list of all data on all customers
     */
    public static ObservableList<Customer> getCustomers(){

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        try{
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Customer c = new Customer(rs.getInt("Customer_ID"),
                                    rs.getString("Customer_Name"),
                                    rs.getString("Address"),
                                    rs.getString("Postal_Code"),
                                    rs.getString("Phone"),
                                    rs.getInt("Division_ID"));
                allCustomers.add(c);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * Method adds a customer
     * @param name name of customer
     * @param address address of a customer
     * @param postal_Code postal code of a customer
     * @param phone phone number of a customer
     * @param divisionName name of division of where a customer is from
     * @return true, customer has been added, false if not
     * @throws SQLException get error message
     */
    public static boolean addCustomer(String name, String address, String postal_Code, String phone,
                               String divisionName) throws SQLException {

   FirstLevelDivision newDiv = FirstLevelDivisionDAO.getDivisionID(divisionName);

   String sql ="INSERT INTO customers(Customer_ID,Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
           "VALUES (?, ?, ?, ?, ?, ?)";
   PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

   // create auto id
    Integer autoCustID = (int) (Math.random()*1000);
   // set the inputted data
    ps.setInt(1,autoCustID);
    ps.setString(2,name);
    ps.setString(3,address);
    ps.setString(4,postal_Code);
    ps.setString(5,phone);
    ps.setInt(6,newDiv.getDivision_id());

    try{
        ps.execute();
        return true;
    }catch (Exception ex){
        System.out.println(ex.getMessage());
        return false;
    }

}

    /**
     * Method updates customer information
     * @param customer_ID id of customer, used to determine which customer to change.
     * @param name name of customer
     * @param address address of customer
     * @param postal_Code postal code of customer
     * @param phone phone number of customer
     * @param divisionName name of division where customer is from
     * @return true if updated, false if not
     * @throws SQLException returns a error message
     */
    public static boolean updateCustomer (int customer_ID, String name, String address, String postal_Code, String phone,
                                          String divisionName) throws SQLException {

        FirstLevelDivision newDiv = FirstLevelDivisionDAO.getDivisionID(divisionName);

        String sql ="UPDATE  customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone =? , Division_ID= ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1,name);
        ps.setString(2,address);
        ps.setString(3,postal_Code);
        ps.setString(4,phone);
        ps.setInt(5,newDiv.getDivision_id());
        ps.setInt(6,customer_ID);

        try{
            ps.execute();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Method deletes a customer by their customer ID
     * @param customerID id of customer
     * @return true if deleted, otherwise false
     */
    public static boolean deleteCustomerByID(int customerID) {
        String sql = "DELETE from customers WHERE Customer_ID =?";

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1,customerID);
            ps.execute();
            return true;

        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

}
