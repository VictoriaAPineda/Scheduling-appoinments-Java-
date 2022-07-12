package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import static DBConnection.JDBC.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contact Class works with data about a contact
 */
public class ContactDAO  {
    // list to hold contacts
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * Method gets a list of all contacts
     * @return Observable list of contacts
     */
    public  static ObservableList<Contact> getContacts (){
        if(allContacts.isEmpty()){ // empty then load, prevents looping
            String sql = "SELECT * FROM contacts";
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                    Contact c = new Contact(rs.getInt("Contact_ID"),
                            rs.getString("Contact_Name"),
                            rs.getString("Email"));
                    allContacts.add(c);
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

        return  allContacts;
    }

}

