package Controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Class modifies/updates a customer's appointment data
 */
public class modifyCustomerApptController implements Initializable {
    Stage stage;
    Parent scence;

    // variables for time conversions
    private ZonedDateTime startDateConvert;
    private ZonedDateTime endDateConvert;

    /**
     * Method to convert local to EST
     * @param t LocalDateTime object
     * @return EST time of the local time
     */
    private ZonedDateTime convertTimeToEST(LocalDateTime t){
        return ZonedDateTime.of(t, ZoneId.of("America/New_York"));
    }

    @FXML
    private TextField apptTitleTxt;

    @FXML
    private ComboBox<String> apptTypeCBox;

    @FXML
    private TextField autoGenApptIDTxt;

    @FXML
    private ComboBox<Contact> contactCbox;

    @FXML
    private ComboBox<Integer> cusomterIDCBox;

    @FXML
    private TextArea descriptionTxt;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> endTimeCBox;

    @FXML
    private TextField locationTxt;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startTimeCBox;

    @FXML
    private ComboBox<User> userIDCBox;

    private static Appointment selectedAppt;


    @FXML
    void onActionContactCBox(ActionEvent event) {

    }

    @FXML
    void onActionCustIDCBox(ActionEvent event) {

    }

    @FXML
    void onActionEndDate(ActionEvent event) {

    }

    @FXML
    void onActionEndTimeCBox(ActionEvent event) {

    }

    /**
     *Back button, takes user back to customer schedules tableview
     * @param event clicks on Back button
     */
    @FXML
    void onActionGoToCustAppts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerSchedules.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Save button, saves the modified/updated data made by the user
     * @param event cics on Save button
     */
    @FXML
    void onActionSave(ActionEvent event) {
        try {
            // Checks to make sure the new changes are valid
            boolean validAppt = check(apptTitleTxt.getText(), descriptionTxt.getText(), locationTxt.getText());

            // if true, will be updated
            if(validAppt){
                boolean modified = AppointmentDAO.updateAppointment(
                        apptTitleTxt.getText(),
                        descriptionTxt.getText(),
                        locationTxt.getText(),
                        apptTypeCBox.getSelectionModel().getSelectedItem(),
                        String.valueOf(contactCbox.getValue().getContact_id()),
                        LocalDateTime.of(startDatePicker.getValue(), LocalTime.parse(startTimeCBox.getSelectionModel().getSelectedItem())),
                        LocalDateTime.of(endDatePicker.getValue(), LocalTime.parse(endTimeCBox.getSelectionModel().getSelectedItem())),
                        cusomterIDCBox.getSelectionModel().getSelectedItem(),
                        userIDCBox.getValue().getUser_id(),
                        Integer.parseInt(autoGenApptIDTxt.getText()));

                if(modified){
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scence = FXMLLoader.load(getClass().getResource("/Views/customerSchedules.fxml"));
                    stage.setScene(new Scene(scence));
                    stage.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please fill out the whole form to edit a customer schedule.");
                alert.showAndWait();
            }

        }catch (Exception ex){
            ex.getMessage();
        }

    }

    /**
     * Check method checks to see if it passes the tests in order to be a valid appointment
     * @param title title of appointment
     * @param description appointment description
     * @param location location of appointment
     * @return true/false if valid of not
     */
    private boolean check (String title, String description, String location){

        // check that nothing is left unfilled/unselected
        if( title.isEmpty() || description.isEmpty() || location.isEmpty()||
                contactCbox.getSelectionModel().isEmpty() || apptTypeCBox.getSelectionModel().isEmpty()||
                startDatePicker.getValue() == null || startTimeCBox.getSelectionModel().isEmpty() ||
                cusomterIDCBox.getSelectionModel().isEmpty() || userIDCBox.getSelectionModel().isEmpty()||
                endDatePicker.getValue() == null || endTimeCBox.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill out all text fields and select the appropriate choice for all boxes.");
            alert.showAndWait();

            return false;
        }

        // Check for valid TIME selections to prevent illogical answers
        LocalTime apptStartTime = LocalTime.parse(startTimeCBox.getSelectionModel().getSelectedItem());
        LocalTime apptEndTime = LocalTime.parse(endTimeCBox.getSelectionModel().getSelectedItem());

        if(apptEndTime.isBefore(apptStartTime)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error - Time Inconsistency");
            alert.setContentText("Start time of appointment must be before end time.");
            alert.showAndWait();
            return false;
        }
        // check for valid DATE selections (appointments must start and end on SAME day.)
        LocalDate apptStartDate = startDatePicker.getValue();
        LocalDate apptEndDate = endDatePicker.getValue();

        if(!apptStartDate.equals(apptEndDate)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error - Date out of bounds");
            alert.setContentText("Start and end day must be the same.");
            alert.showAndWait();
            return false;
        }

        try {

            // check for overlapping of appointments
            LocalDateTime startSelected = apptStartDate.atTime(apptStartTime); // start time of appointment
            LocalDateTime endSelected = apptEndDate.atTime(apptEndTime); // end time of appointment

            LocalDateTime listedApptStart;
            LocalDateTime listedApptEnd;

            // list of appts. of a customer by their id
            ObservableList<Appointment> appts = AppointmentDAO.getApptsByCustomerID(cusomterIDCBox.getSelectionModel().getSelectedItem());
            int apptID = Integer.parseInt(autoGenApptIDTxt.getText());
            for (Appointment a : appts) {// loops through list of appts
                if(a.getAppointment_id()== apptID){
                    continue;
                }
                listedApptStart = a.getStart();
                listedApptEnd = a.getEnd();

                // Situations that cause overlapping of appointments
                // condition # 1
                if (listedApptStart.isAfter(startSelected) && listedApptStart.isBefore(endSelected)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Unable to schedule appointment");
                    alert.setContentText("Appointment overlaps with another.");
                    alert.showAndWait();
                    return false;
                }
                // condition # 2
                else if ( (startSelected.isEqual(listedApptStart)) || (endSelected.isEqual(listedApptEnd)) ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Unable to schedule appointment");
                    alert.setContentText("Appointment overlaps with another.");
                    alert.showAndWait();
                    return false;
                }
                // condition # 3
                else if( (startSelected.isAfter(listedApptStart)) && (startSelected.isBefore(listedApptEnd)) ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Unable to schedule appointment");
                    alert.setContentText("Appointment overlaps with another.");
                    alert.showAndWait();
                    return false;
                }
                // condition # 4
                else if ( (startSelected.isBefore(listedApptStart)) && (endSelected.isAfter(listedApptEnd)) ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Unable to schedule appointment");
                    alert.setContentText("Appointment overlaps with another.");
                    alert.showAndWait();
                    return false;
                }
                // condition #5
                else if (listedApptEnd.isAfter(startSelected) && listedApptEnd.isBefore(endSelected)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Unable to schedule appointment");
                    alert.setContentText("Appointment overlaps with another.");
                    alert.showAndWait();
                    return false;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        // check if appointment is scheduled between business hours (EST TIME)
        // convert the local time to EST
        startDateConvert = convertTimeToEST(LocalDateTime.of(startDatePicker.getValue(),
                LocalTime.parse(startTimeCBox.getSelectionModel().getSelectedItem())));
        endDateConvert = convertTimeToEST(LocalDateTime.of(endDatePicker.getValue(),
                LocalTime.parse(endTimeCBox.getSelectionModel().getSelectedItem())));

        if(startDateConvert.toLocalTime().isAfter(LocalTime.of(22,0))){ // 10 pm
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time Error");
            alert.setContentText("Appointments can only be scheduled between the hours of " +
                    "8:00am - 10:00pm EST time. This also includes the weekends.");
            alert.showAndWait();
            return false;
        }
        if(endDateConvert.toLocalTime().isAfter(LocalTime.of(22,0))){ // 10 pm
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time Error");
            alert.setContentText("Appointments can only be scheduled between the hours of " +
                    "8:00am - 10:00pm EST time. This also includes the weekends.");
            alert.showAndWait();
            return false;
        }
        if(startDateConvert.toLocalTime().isBefore(LocalTime.of(8,0))){ // 8 am
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Time Error");
            alert.setContentText("Appointments can only be scheduled between the hours of " +
                    "8:00am - 10:00pm EST time. This also includes the weekends.");
            alert.showAndWait();
            return false;
        }
        if(endDateConvert.toLocalTime().isBefore(LocalTime.of(8,0))){ // 8 am
            Alert alert = new Alert(Alert.AlertType.ERROR);alert.setTitle("Time Error");
            alert.setContentText("Appointments can only be scheduled between the hours of " +
                    "8:00am - 10:00pm EST time. This also includes the weekends.");
            alert.showAndWait();
            return false;
        }

        // if passes ALL tests
        return  true;
    }

    @FXML
    void onActionStartDate(ActionEvent event) {

    }

    @FXML
    void onActionStartTimeCBox(ActionEvent event) {

    }

    @FXML
    void onActionTypeCBox(ActionEvent event) {

    }

    @FXML
    void onActionUserIDCBox(ActionEvent event) {

    }

    /**
     * Bring in data from the table into the modify customer appointment form,
     * Pre-populates the form.
     * @param a Appointment object
     */
    public static void sendSelectedAppointment (Appointment a){
        selectedAppt = a;
    }

    /**
     * Initializes the modify customer form with pre-written/selected data
     * Lambda #8 gets customer id from each customer object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // display pre-populated form

            autoGenApptIDTxt.setText(Integer.toString(selectedAppt.getAppointment_id()));
            apptTitleTxt.setText(selectedAppt.getTitle());
            descriptionTxt.setText(selectedAppt.getDescription());
            locationTxt.setText(selectedAppt.getLocation());

            apptTypeCBox.getSelectionModel().select(selectedAppt.getType());
            cusomterIDCBox.getSelectionModel().select(Integer.valueOf(selectedAppt.getCustomer_id()));

            startDatePicker.setValue(selectedAppt.getStart().toLocalDate());
            startTimeCBox.setValue(String.valueOf(selectedAppt.getStart().toLocalTime()));

            endDatePicker.setValue(selectedAppt.getEnd().toLocalDate());
            endTimeCBox.setValue(String.valueOf(selectedAppt.getEnd().toLocalTime()));

            // display contact names in combo box
           contactCbox.setItems(ContactDAO.getContacts());

            ObservableList<Contact> allContacts = ContactDAO.getContacts();
            Contact showContact = null;
            for (Contact c:allContacts){
                if(selectedAppt.getContact_id()== c.getContact_id()){ // matches the contactIDs
                    showContact = c;
                }
            }
            contactCbox.setValue(showContact);// pre-populate the matched contact's name

        //prepopulate the user id combo box
        userIDCBox.setItems(UserDAO.getUsers());

        ObservableList<User> allUsers = UserDAO.getUsers();
        User showUser = null;
        for(User u: allUsers){
            if(selectedAppt.getUser_id() == u.getUser_id()){
                showUser = u;
            }
        }
        userIDCBox.setValue(showUser);// pre-populates the user id combo box

        // initialize appointment type combo box
        apptTypeCBox.setItems(AppointmentDAO.getAllApptTypes());

        // customer id combo box
        ObservableList<Customer> allCustomers = CustomerDAO.getCustomers();
        ObservableList<Integer> allCustomersID = FXCollections.observableArrayList();
        // lambda #8
        allCustomers.forEach(c->allCustomersID.add(c.getCustomer_id()));
        cusomterIDCBox.setItems(allCustomersID);

        // populate the time combo boxes
        ObservableList<String> apptTimes = FXCollections.observableArrayList();

        LocalTime beginAppts = LocalTime.of(5,0);// start of time selections
        LocalTime endAppts = LocalTime.of(23,0);// end of time selections

        while(beginAppts.isBefore(endAppts)){
            apptTimes.add(String.valueOf(beginAppts));
            beginAppts = beginAppts.plusMinutes(15);// 15 min increments
        }
        startTimeCBox.setItems(apptTimes);
        endTimeCBox.setItems(apptTimes);

    }
}
