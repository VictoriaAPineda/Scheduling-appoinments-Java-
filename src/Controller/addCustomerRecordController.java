package Controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDivisionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class adds a customer record to table
 */
public class addCustomerRecordController implements Initializable {
    Stage stage;
    Parent scence;

    @FXML
    private TextField custAddrTxt;

    @FXML
    private TextField custNameTxt;

    @FXML
    private TextField custPhoneTxt;

    @FXML
    private TextField custPostalCodeTxt;

    @FXML
    private ComboBox<String> firstLvlDivCBox; // will be determined by the country combo box

    @FXML
    private ComboBox<String> countryCBox;



    @FXML
    /**
     *  Country Combo/drop-down
     *  Records which country the user chooses, and by that choice, determines which
     *  first-level divisions will be displayed in the firstLvlDivBox (combo box)
     *  Lambda #2 sorts divisions into a specific country's divisions list
     */
    void onActionCountryCBox(ActionEvent event) {

        try {
            // gets what the user selects from combo box
            String country = countryCBox.getSelectionModel().getSelectedItem();
            // retrieves all the divisions
            ObservableList<FirstLevelDivision> allFirstLvlDivs = FirstLevelDivisionDAO.getFirstLevelDivisions();
            // creates a separate list for each country's divisions
            ObservableList<String> USDivs = FXCollections.observableArrayList();
            ObservableList<String> UKDivs = FXCollections.observableArrayList();
            ObservableList<String> CADivs = FXCollections.observableArrayList();

            // goes through the divisions list and places/sorts them into a country's division list
            // based on their country's id
            // lambda #2
            allFirstLvlDivs.forEach(div -> {
                if (div.getCountry_id() == 1) {
                    USDivs.add(div.getDivision_name());
                } else if (div.getCountry_id() == 2) {
                    UKDivs.add(div.getDivision_name());
                } else if (div.getCountry_id() == 3) {
                    CADivs.add(div.getDivision_name());
                }
            });
            // depending on the country the user selected, will adjust the
            // divisions viewable in the division combo box
            if (country.equals("U.S")) { // equals the name in  sql database
                firstLvlDivCBox.setItems(USDivs);
            }
            else if (country.equals("UK")) {
                firstLvlDivCBox.setItems(UKDivs);
            }
            else if (country.equals("Canada")) {
                firstLvlDivCBox.setItems(CADivs);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML// no need to program this, this box relies on the
    // selected country from the country combo box
    void onActionFirstLvlDivCBox(ActionEvent event) {
    }

    @FXML
    /**
     * Cancel button, when clicked, takes user back to customer records tableview
     * @param event button click on Cancel
     */
    void onActionReturnBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerRecords.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    @FXML
    /**
     * Save button, commits a new customer record to the table
     * based on what the user has inputted into the Add Customer Form
     * @param event button click on Save
     */
    void onActionSaveBtn(ActionEvent event) {
        try {
            // returns true/false depending if form is filled out
            boolean notEmpty = check(custNameTxt.getText(),custAddrTxt.getText(),
                    custPostalCodeTxt.getText(), custPhoneTxt.getText());
            // if true will then add customer to database
            if(notEmpty){
                boolean added = CustomerDAO.addCustomer(custNameTxt.getText(),
                        custAddrTxt.getText(), custPostalCodeTxt.getText(),
                                custPhoneTxt.getText(),firstLvlDivCBox.getValue());


                if(added){
                // takes user back to tableview of customer records
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scence = FXMLLoader.load(getClass().getResource("/Views/customerRecords.fxml"));
                stage.setScene(new Scene(scence));
                stage.show();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please fill out the whole form to add a customer to records.");
                alert.showAndWait();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Check method is used to make sure everything in the form has data in it and
     * all combo boxes have a selection
     * @param name customer's name
     * @param address address of customer
     * @param postal_code postal code of customer
     * @param phone phone number of custoemr
     * @return true or false
     */
    private boolean check(String name, String address, String postal_code, String phone){
        if(name.isEmpty() || address.isEmpty() || postal_code.isEmpty()
        || phone.isEmpty() || countryCBox.getSelectionModel().isEmpty() || firstLvlDivCBox.getSelectionModel().isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    /**
     * Initializes the combo boxes
     * Lambda #4 gets country names from each Country object in list
     * Lambda #5 gets division names from each FirstLevelDivision object in list
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // display pre-populated  data in country and division combo boxes
        ObservableList<Country> allCountries = CountryDAO.getCountries();
        ObservableList<String> allCountryNames = FXCollections.observableArrayList();

        ObservableList<FirstLevelDivision> allDivs = FirstLevelDivisionDAO.getFirstLevelDivisions();
        ObservableList<String> allDivNames = FXCollections.observableArrayList();

        // for each country, gets their names and set them in combo box
        // lambda #4
        allCountries.forEach(c-> allCountryNames.add(c.getCountry_Name()));
        countryCBox.setItems(allCountryNames);

        // for each division, gets their names and set them in combo box
        // lambda #5
        allDivs.forEach(d -> allDivNames.add(d.getDivision_name()));
        firstLvlDivCBox.setItems(allDivNames);

    }

}
