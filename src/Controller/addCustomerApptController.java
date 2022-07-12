package Controller;

import DAO.*;
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
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Class adds customer appointments
 */
public class addCustomerApptController implements Initializable {
    Stage stage;
    Parent scence;

    // To convert local time to EST time
    private ZonedDateTime startDateConvert;
    private ZonedDateTime endDateConvert;

    private ZonedDateTime convertTimeToEST(LocalDateTime t){
        return ZonedDateTime.of(t, ZoneId.of("America/New_York"));
    }


    @FXML
    private TextField apptTitleTxt;

    @FXML
    private ComboBox<Contact> contactCBox;

    @FXML
    private ComboBox<Integer> custIDCBox;

    @FXML
    private TextArea descriptionTxt;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField locationtxt;
    @FXML
    private ComboBox<String> endTimeCBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startTimeCBox;

    @FXML
    private ComboBox<String> typeCBox;

    @FXML
    private ComboBox<User> userIDCBox;



    /**
     * Cancel button takes user back to schedule tableview
     */
    @FXML
    void onActionGoToCustAppts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerSchedules.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Save button saves data from the add customer appointment form to the schedule tableview.
     * Also saves it to a customer
     * @param event
     * @throws SQLException error message
     */
    @FXML
    void onActionSave(ActionEvent event) throws SQLException {
        try {
            // check to see if the input is valid (true/false)
            boolean validAppt = check(apptTitleTxt.getText(), descriptionTxt.getText(), locationtxt.getText());

            // if true -  all data is correctly inputted
            if (validAppt) {
                // goes to add the appointment, if successful true/false
                boolean added = AppointmentDAO.addAppointment(
                        apptTitleTxt.getText(),
                        descriptionTxt.getText(),
                        locationtxt.getText(),
                        typeCBox.getSelectionModel().getSelectedItem(),
                        String.valueOf(contactCBox.getValue().getContact_id()),
                        LocalDateTime.of(startDatePicker.getValue(), LocalTime.parse(startTimeCBox.getSelectionModel().getSelectedItem())),
                        LocalDateTime.of(endDatePicker.getValue(), LocalTime.parse(endTimeCBox.getSelectionModel().getSelectedItem())),
                        custIDCBox.getSelectionModel().getSelectedItem(),
                        userIDCBox.getValue().getUser_id());

                if(added){// (true) goes back to table view
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scence = FXMLLoader.load(getClass().getResource("/Views/customerSchedules.fxml"));
                    stage.setScene(new Scene(scence));
                    stage.show();
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to add appointment.");
                alert.showAndWait();
            }
        }catch (Exception ex){
            ex.getMessage();
        }
    }

    /**
     * Method used to check the validity of the data inputted into add customer appointment form
     * @param title title of appointment
     * @param description appointment description
     * @param location location of appointment
     * @return true/false if it passes the tests
     */
    private boolean check (String title, String description, String location){
        // checks for empty/no input
        if( title.isEmpty() || description.isEmpty() || location.isEmpty()||
        contactCBox.getSelectionModel().isEmpty() || typeCBox.getSelectionModel().isEmpty()||
        startDatePicker.getValue() == null || startTimeCBox.getSelectionModel().isEmpty() ||
        custIDCBox.getSelectionModel().isEmpty() || userIDCBox.getSelectionModel().isEmpty()||
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
        // check for valid DATE selections (appts. must start and end on SAME day.)
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
    LocalDateTime startSelected = apptStartDate.atTime(apptStartTime); // start time of appt user wants
    LocalDateTime endSelected = apptEndDate.atTime(apptEndTime); // end time of appt user wants

    LocalDateTime listedApptStart; // appointment already listed with that customer
    LocalDateTime listedApptEnd;

    // list of Appts. of a customer by their id
    ObservableList<Appointment> appts = AppointmentDAO.getApptsByCustomerID(custIDCBox.getSelectionModel().getSelectedItem());

    for (Appointment a : appts) {// loops through list of appts
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
        // check if appt is scheduled between business hours (EST TIME)
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


        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //prepopulate the user id combo box
        userIDCBox.setItems(UserDAO.getUsers());
        // display contact names
        contactCBox.setItems(ContactDAO.getContacts());
        // appt type combo box
        typeCBox.setItems(AppointmentDAO.getAllApptTypes());

        // customer id combo box
        ObservableList<Customer> allCustomers = CustomerDAO.getCustomers();
        ObservableList<Integer> allCustomersID = FXCollections.observableArrayList();
        allCustomers.forEach(c->allCustomersID.add(c.getCustomer_id()));
        custIDCBox.setItems(allCustomersID);

        // populate the time combo boxes
        ObservableList<String> apptTimes = FXCollections.observableArrayList();

        LocalTime beginAppts = LocalTime.of(5,0);
        LocalTime endAppts = LocalTime.of(23,0);

        while(beginAppts.isBefore(endAppts)){
                apptTimes.add(String.valueOf(beginAppts));
                beginAppts = beginAppts.plusMinutes(15);// 15 min increments
;            }
        startTimeCBox.setItems(apptTimes);
        endTimeCBox.setItems(apptTimes);

    }
}
