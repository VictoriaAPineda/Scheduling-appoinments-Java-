package Controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class display customer records in table
 */
public class custRecController implements Initializable {
    Stage stage;
    Parent scence;

    // observable list to hold customers
    static ObservableList<Customer> customers;

    @FXML
    private TableColumn<?, String> addressCol;

    @FXML
    private TableColumn<?, Integer> custIDCol;

    @FXML
    private TableView<Customer> custRecordsTableView;

    @FXML
    private TableColumn<?, String> firstLvlDivCol;

    @FXML
    private TableColumn<?, String> nameCol;

    @FXML
    private TableColumn<?, String> phoneCol;

    @FXML
    private TableColumn<?, Integer> postalCodeCol;

    /**
     * Add Button takes user to the add customer record form
     * @param event click on Add button
     */
    @FXML
    void onActionAddCustRec(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/addCustomerRecord.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Back button takes user to main menu
     * @param event click on Back button
     * @throws IOException
     */
    @FXML
    void onActionBackToMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/mainMenu.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * checkCustAppts method will check to see if the customer has any appointments.
     * If there are will return false, making user unable to delete customer.
     * If true, customer can be deleted
     * @param selected a Customer object that is based on the row the user selects on tableview
     * @return true or false
     */
    private boolean checkCustAppts(Customer selected){
        try {
            // list to hold appointment based on customer id
            ObservableList appts = AppointmentDAO.getApptsByCustomerID(selected.getCustomer_id());
            // checks the size of the amount of appointments customer has
            if (appts.size() < 1) {
                return true;
            } else {
                return false;
            }
        }catch (Exception ex){
            ex.getMessage();
            return false;
        }
    }
    /**
     * Deletes a customer record from table
     * Must have checkCustAppts return true in order to delete
     */
    @FXML
    void onActionDeleteCustRec(ActionEvent event) {
        // users chooses which customer to delete on table
        Customer selectedCustomer = custRecordsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setContentText("Are you sure wish to delete this customer?");
        Optional<ButtonType> input = alert.showAndWait();

        if(input.isPresent() && (input.get() == ButtonType.OK)){
            // check that the selected customer has no appointments
            boolean readyToDelete = checkCustAppts(selectedCustomer);
            // if true, then delete
            if(readyToDelete){
                    boolean deleted = CustomerDAO.deleteCustomerByID(custRecordsTableView.getSelectionModel().getSelectedItem().getCustomer_id());
                    // true if it has been successfully deleted
                    if(deleted){
                        // refresh/update tableview
                        customers = CustomerDAO.getCustomers();
                        custRecordsTableView.setItems(customers);
                        custRecordsTableView.refresh();
                    }
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Error");
                alert.setContentText("You are not allowed to delete customers with appointments.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Modify Button
     * User selects a customer row they want to modify data, sends the selection
     * of data to the modify customer form
     * @param event clicking on the Modify button
     */
    @FXML
    void onActionModifyCustRec(ActionEvent event) throws IOException {
        // sends the data that user selects to the modify customer form
        modifyCustomerRecordController.sendSelectedCustomer(custRecordsTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/modifyCustomerRecord.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Initializes the customer record tableview
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    // initializes the records of customers in the table view
        // list to get all customer and their data to set into table
        ObservableList<Customer> allCustomers = CustomerDAO.getCustomers();
        custRecordsTableView.setItems(allCustomers);

      custIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));// name matches the Customer model class
      nameCol.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
      addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
      postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal_Code"));
      phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
      firstLvlDivCol.setCellValueFactory(new PropertyValueFactory<>("division_id"));


    }
}
