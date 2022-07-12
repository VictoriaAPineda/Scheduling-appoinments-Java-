package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;
import static DBConnection.JDBC.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class work tih data about first level division of a country
 */
public class FirstLevelDivisionDAO  {


    private  static ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();

    /**
     * Method gets all the first level divisions
     * @return Observable list of all first level divisions
     */
    public static ObservableList<FirstLevelDivision> getFirstLevelDivisions (){
        if(allFirstLevelDivisions.isEmpty()){// checks to see if empty first, prevents repeating selections
            String sql = "SELECT * FROM First_level_divisions";
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    FirstLevelDivision f = new FirstLevelDivision(rs.getInt("Division_ID"),
                            rs.getString("Division"),
                            rs.getInt("Country_ID"));
                    allFirstLevelDivisions.add(f);
                }

            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

        return allFirstLevelDivisions;
    }

    /**
     * Method gets division id by a division name
     * @param divisionName name of division
     * @return newDiv
     */
    public static FirstLevelDivision getDivisionID(String divisionName) {
        String sql = "SELECT * FROM first_level_divisions WHERE Division=?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, divisionName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FirstLevelDivision newDiv = new FirstLevelDivision(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        rs.getInt("Country_ID")
                );
                return newDiv;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


}
