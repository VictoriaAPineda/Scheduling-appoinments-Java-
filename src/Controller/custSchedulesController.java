package Controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
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
import model.Appointment;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class displays customer appointments
 */
public class custSchedulesController implements Initializable {
    Stage stage;
    Parent scence;

    // list to hold appointments
    static ObservableList<Appointment> appts;

    @FXML
    private RadioButton allRBtn;

    @FXML
    private TableColumn<?, Integer> apptIDCol;

    @FXML
    private TableColumn<?, Integer> contactCol;

    @FXML
    private TableColumn<?, Integer> custIDCol;

    @FXML
    private TableView<Appointment> custSchedulesTableView;

    @FXML
    private TableColumn<?, String> descriptionCol;

    @FXML
    private TableColumn<?, DatePicker> endDateTimeCol;

    @FXML
    private TableColumn<?, String> locationCol;

    @FXML
    private RadioButton monthRBtn;

    @FXML
    private TableColumn<?, DatePicker> startDateTimeCol;

    @FXML
    private TableColumn<?, String> titleCol;

    @FXML
    private TableColumn<?, String> typeCol;

    @FXML
    private TableColumn<?, Integer> userIDCol;

    @FXML
    private ToggleGroup viewButtonGroup;

    @FXML
    private RadioButton weekRBtn;

    @FXML
    /**
     * Add button, takes user to the add customer appointment form
     * @param event clicks on Add button
     */
    void onActionAddCustAppt(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/addCustomerAppt.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    @FXML
    /**
     * Back button, takes user back to the main menu
     * @param event clicks the Back button
     */
    void onActionBackToMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/mainMenu.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    @FXML
    /**
     * User selects a appointment from tableview then clicks Delete button
     * to remove the appointment
     * @param event clicks the Delete button
     */
    void onActionDeleteCustAppt(ActionEvent event) {
        // the appointment the user has selected
       Appointment selectedAppt = custSchedulesTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setContentText("Are you sure wish to delete this customer's appointment?");
        Optional<ButtonType> input = alert.showAndWait();

        if(input.isPresent() && (input.get() == ButtonType.OK)){
            // true if it has been deleted successfully, false if not
            boolean deleted = AppointmentDAO.deleteAppointment(custSchedulesTableView.getSelectionModel().getSelectedItem().getAppointment_id());

            if(deleted){
              alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("Appointment deleted");
              alert.setContentText("Appointment succesfully deleted." +"\n"+"Appointment ID : " + selectedAppt.getAppointment_id() +
                      " Type: " + selectedAppt.getType() );
              alert.showAndWait();

                // automatically refreshes the table
                appts = AppointmentDAO.getAppointments(); // get all appointments list
                custSchedulesTableView.setItems(appts); // set the changes
                custSchedulesTableView.refresh();// reloads to updated view
            }

        }
    }

    @FXML
    // btn to Modify Customer Appointment Form
    /**
     * Modify Button, takes user to the modify appointment form
     * @param event clicks on the Modify button
     */
    void onActionModifyCustAppt(ActionEvent event) throws IOException {
       // send the selected data from the table to the modify form
        modifyCustomerApptController.sendSelectedAppointment(custSchedulesTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/modifyCustomerAppt.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    @FXML
    /**
     * View All appointments
     * @param event click on All radio button
     */
    void onActionViewAll(ActionEvent event) {
        ObservableList<Appointment> allAppts = AppointmentDAO.getAppointments() ;

        if(allAppts != null){
            for(Appointment appt : allAppts){
                custSchedulesTableView.setItems(allAppts);
            }
        }
    }

    @FXML
    /**
     * Views appointments within a month/range of 30 days from today
     * Lambda #6 for each appointment, add those who are within a 30 day range
     * into apptsForMonth list
     * @param event clicking the Month radio button
     */
    void onActionViewByMonth(ActionEvent event) {
        ObservableList<Appointment> allAppts = AppointmentDAO.getAppointments();
        ObservableList<Appointment> apptsForMonth = FXCollections.observableArrayList();

        LocalDateTime beginMonth = LocalDateTime.now().minusMonths(1);// range start
        LocalDateTime endMonth = LocalDateTime.now().plusMonths(1);// range end

        if(allAppts != null){
            // lambda #6
            allAppts.forEach(appt ->{
                if(appt.getEnd().isAfter(beginMonth) && appt.getEnd().isBefore(endMonth)){
                    apptsForMonth.add(appt);
                }
                custSchedulesTableView.setItems(apptsForMonth);// displays appointments within the range
            });
        }

    }

    @FXML
    /**
     * View appointment by a range of a week
     * Lambda #7 for each appointments, adds those that are wihtin a week range
     * into apptsForWeek list
     * @param event clicks on the Week radio button
     */
    void onActionViewByWeek(ActionEvent event) {
        ObservableList<Appointment> allAppts = AppointmentDAO.getAppointments();
        ObservableList<Appointment> apptsForWeek = FXCollections.observableArrayList();

        LocalDateTime beginWeek = LocalDateTime.now().minusWeeks(1);
        LocalDateTime endWeek = LocalDateTime.now().plusWeeks(1);

        if(allAppts != null){
            // lambda #7
            allAppts.forEach(appt->{
                if(appt.getEnd().isAfter(beginWeek) && appt.getEnd().isBefore(endWeek)){
                   apptsForWeek.add(appt);
                }
                custSchedulesTableView.setItems(apptsForWeek);
            });
        }
    }


    @Override
    /**
     * Initializes the customer schedules table
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setup for initializing the table with data
      ObservableList<Appointment> allAppointments = AppointmentDAO.getAppointments();
      custSchedulesTableView.setItems(allAppointments);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
    }
}
