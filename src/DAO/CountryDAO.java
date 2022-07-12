package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.User;
import static DBConnection.JDBC.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {

    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    /**
     * Method gets a list of countries
     * @return Observable list of all countries
     */
    public static ObservableList<Country> getCountries(){
       if(allCountries.isEmpty()){//checks to see if empty first, prevents repeating selections
           String sql = "SELECT * FROM countries";
           try {
               // able to reach into the database to get the info
               PreparedStatement ps = connection.prepareStatement(sql);
               ResultSet rs = ps.executeQuery();
               while (rs.next()){ // scans through the table
                   Country c = new Country(rs.getInt("Country_ID"),rs.getString("Country"));
                   allCountries.add(c);// adds country to list
               }

           } catch (SQLException ex){
               ex.printStackTrace();
           }
       }

        return allCountries;
    }

}
