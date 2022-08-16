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
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class modifies/updates customer records data
 */
public class modifyCustomerRecordController implements Initializable {
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
    private TextField custGenID;

    private static Customer selectedCustomer;

    @FXML
    private ComboBox<String> countryCBox;

    @FXML
    private ComboBox<String> firstLvlCBox;

    @FXML
    /**
     * Country drop-down box
     * selection will affect what is displayed in the division drop down box
     * Lambda #9 sorts each divisions into a specific country's allFirstDivs list
     * based on their country's id
     * @param event user selects a country
     */
    void onActionCountryCBox(ActionEvent event) {
        try {
            // gets what the user selects from combo box
            String country = countryCBox.getSelectionModel().getSelectedItem();

            ObservableList<FirstLevelDivision> allFirstLvlDivs = FirstLevelDivisionDAO.getFirstLevelDivisions();

            ObservableList<String> USDivs = FXCollections.observableArrayList();
            ObservableList<String> UKDivs = FXCollections.observableArrayList();
            ObservableList<String> CADivs = FXCollections.observableArrayList();

            // goes through each divisions on list, sorts them into a country's div based on their
            // country id
            //lambda #9
            allFirstLvlDivs.forEach(div -> {
                if (div.getCountry_id() == 1) {
                    USDivs.add(div.getDivision_name());// adds name to the corresponding division list
                } else if (div.getCountry_id() == 2) {
                    UKDivs.add(div.getDivision_name());
                } else if (div.getCountry_id() == 3) {
                    CADivs.add(div.getDivision_name());
                }
            });
            // based on choice, display a division list
            if (country.equals("U.S")) {
                firstLvlCBox.setItems(USDivs);
            }
            else if (country.equals("UK")) {
                firstLvlCBox.setItems(UKDivs);
            }
            else if (country.equals("Canada")) {
                firstLvlCBox.setItems(CADivs);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void onActionFirstLvlDivCBox(ActionEvent event) {

    }

    @FXML
    /**
     * Back button, takes user back to customer records view
     * @param event clicks on Back button
     */
    void onActionReturnBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerRecords.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Checks to see if everything in modify form is filled/selected
     * @param name name of customer
     * @param address address of customer
     * @param postal_code postal code of customer
     * @param phone phone number of customer
     * @return true/false
     */
    private boolean check(String name, String address, String postal_code, String phone){
        if(name.isEmpty() || address.isEmpty() || postal_code.isEmpty()
                || phone.isEmpty() || countryCBox.getSelectionModel().isEmpty() ||
        firstLvlCBox.getSelectionModel().isEmpty()){
            return false;
        }
        return true;
    }

    @FXML
    /**
     * Save button, saves the modified customer records data to database
     * @param event clicks on Save button
     */
    void onActionSaveBtn(ActionEvent event) {
        try {
            // value true if not empty, false if there are empty fields
            boolean notEmpty = check(custNameTxt.getText(),custAddrTxt.getText(),
                    custPostalCodeTxt.getText(), custPhoneTxt.getText());
            //if true updates
            if(notEmpty){
                boolean modified = CustomerDAO.updateCustomer(Integer.parseInt(custGenID.getText()),custNameTxt.getText(),
                        custAddrTxt.getText(), custPostalCodeTxt.getText(),
                        custPhoneTxt.getText(),firstLvlCBox.getValue());

                if(modified){ // takes user to customer records tableview to view changes
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scence = FXMLLoader.load(getClass().getResource("/Views/customerRecords.fxml"));
                    stage.setScene(new Scene(scence));
                    stage.show();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please fill out the whole form to edit customer records.");
                alert.showAndWait();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Bring  in data from the table into the modify customer form,
     * Pre-populates the form.
     * @param c customer object
     */
    public  static void sendSelectedCustomer(Customer c){
        selectedCustomer = c;
    }

    @Override
    /**
     * Initializes the modify customer form with pre-written/selected data
     * Lambda #10 gets the name from each country object
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Display the existing customer info / pre-populates
        custGenID.setText(Integer.toString(selectedCustomer.getCustomer_id()));
        custNameTxt.setText(selectedCustomer.getCustomer_name());
        custAddrTxt.setText(selectedCustomer.getAddress());

        firstLvlCBox.getSelectionModel().select(selectedCustomer.getDivision_id());
        custPostalCodeTxt.setText(selectedCustomer.getPostal_Code());
        custPhoneTxt.setText(selectedCustomer.getPhone());

        // initialize combo boxes
        ObservableList<Country> allCountries = CountryDAO.getCountries();
        ObservableList<String> allCountryNames = FXCollections.observableArrayList();

        ObservableList<FirstLevelDivision> allDivs = FirstLevelDivisionDAO.getFirstLevelDivisions();
        ObservableList<String> allDivNames = FXCollections.observableArrayList();
        // lambda #10
        allCountries.forEach(c-> allCountryNames.add(c.getCountry_Name()));
        countryCBox.setItems(allCountryNames);
        String countryName= "";
        String divName= "";
        int countryID1 =0;
        for(FirstLevelDivision div1:allDivs){
            if(div1.getDivision_id()==selectedCustomer.getDivision_id()){
                countryID1 = div1.getCountry_id();
                divName = div1.getDivision_name();
                for (Country c: allCountries){
                    if(c.getCountry_id() == countryID1){// match the country id with teh division's country id
                        countryName =c.getCountry_Name();
                        break;// once found break out
                    }
                }
                break;
            }
        }

        for(FirstLevelDivision div1:allDivs){
            if(div1.getCountry_id()== countryID1){
                allDivNames.add(div1.getDivision_name());
                // display only the list of division names based on current
                // selected country
            }
        }

        firstLvlCBox.setItems(allDivNames);// displays current division list
        firstLvlCBox.setValue(divName);// display currently selected division
        countryCBox.setValue(countryName);// displays current country selected
    }
}
