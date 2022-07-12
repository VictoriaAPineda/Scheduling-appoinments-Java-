package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import static DBConnection.JDBC.connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class works with information about a user
 */
public class UserDAO {


    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * Method gets all users and their data
     * @return Observable list of users
     */
    public static  ObservableList<User> getUsers(){
        if(allUsers.isEmpty()){ // if empty load it!
            try{
                String sql= "SELECT * FROM users";
                PreparedStatement ps = connection.prepareStatement(sql); //
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    User u = new User(rs.getInt("User_Id"),
                            rs.getString("User_Name"),
                            rs.getString("Password"));
                    allUsers.add(u);// adds row
                }

            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }

        return allUsers;
    }

    /**
     * Method used to check login credentials to check that the user is valid
     * @param username user's username
     * @param password user's password
     * @return int User_ID, -1 if incorrect
     */
    public static int validateUser(String username, String password){
        // query for the login info checking to the db
        String sql = "SELECT User_ID, User_Name, Password " +
                "FROM users " +
                "WHERE User_Name = '"+username+"' AND Password = '"+password+"' ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                if(rs.getString("User_Name").equals(username)){
                    if(rs.getString("Password").equals(password)){
                        return rs.getInt("User_ID");

                    }
                }
            }

        }catch (SQLException ex ){
            ex.printStackTrace();
        }
        return -1; // if it's no valid
    }


}
