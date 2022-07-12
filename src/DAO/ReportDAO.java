package DAO;

import DBConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ReportCountry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class works with report based on the SQL database information
 */
public class ReportDAO {

    /**
     * Method gets country info, country name and number the of customers per country
     * @return repCountry country report information
     * @throws SQLException
     */
    public static ObservableList<ReportCountry> getCountries() throws SQLException {
        ObservableList<ReportCountry> countryList = FXCollections.observableArrayList();
        String sql ="select countries.Country, count(*) as countCountry from customers \n" +
                "inner join first_level_divisions \n" +
                "on customers.Division_ID = first_level_divisions.Division_ID \n" +
                "inner join countries \n" +
                "on countries.Country_ID = first_level_divisions.Country_ID \n" +
                "where  customers.Division_ID = first_level_divisions.Division_ID \n" +
                "group by first_level_divisions.Country_ID\n" +
                "order by Country asc ";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ReportCountry repCountry = new ReportCountry(
                        rs.getString("Country"),
                        rs.getInt("countCountry")
                );
                countryList.add(repCountry);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return countryList;
    }

}
