package Controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.ReportDAO;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Class displays various reports based on customer and appointment data
 */
public class reportsController implements Initializable {
    Stage stage;
    Parent scence;

    @FXML
    private ComboBox<String> contactCBox;

    @FXML
    private TableColumn<?, Integer> apptIDCol;

    @FXML
    private TableColumn<?, String> monthColR2;

    @FXML
    private TableColumn<?, Integer> totalMonthR2;

    @FXML
    private TableView<ReportByMonth> monthTotalApptTableView;

    @FXML
    private TableColumn<?, String> typeColR2;

    @FXML
    private TableColumn<?, Integer> totalTypeR2;

    @FXML
    private TableView<ReportByType> typeTotalApptTableView;

    @FXML
    private TableView<Appointment> contactSchedulesTableView;

    @FXML
    private TableColumn<?, String> countryColR3;

    @FXML
    private TableColumn<?, Integer> totalR3;

    @FXML
    private TableView<ReportCountry> countryTotalTableView;

    @FXML
    private TableColumn<?, Integer> customerIDCol;

    @FXML
    private TableColumn<?, String> descriptionCol;

    @FXML
    private TableColumn<?, DatePicker> endDateTimeCol;

    @FXML
    private TableColumn<?, DatePicker> startDateTimeCol;

    @FXML
    private TableColumn<?, String> titleCol;

    @FXML
    private TableColumn<?, String> typeCol;

    /**
     * Back button, takes user back to the main menu
     * @param event clicks on back button
     */
    @FXML
    void onActionBackToMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/mainMenu.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Report #1
     * Contact box, user selects a contact and table will display all
     * appointments that contact has.
     * @param event selects a contact's name from drop down box
     */
    @FXML
    void onActionContactCBox(ActionEvent event) {
        try {
            // contact selected
            String contactName = contactCBox.getSelectionModel().getSelectedItem();
            int contactID = 0; // default

            ObservableList<Appointment> getAllApptInfo = AppointmentDAO.getAppointments();
            ObservableList<Appointment> apptData = FXCollections.observableArrayList();
            ObservableList<Contact> getAllContacts = ContactDAO.getContacts();

            Appointment apptContact;// appointment object

            for (Contact c : getAllContacts) {// scans through contact list
                if (contactName.equals(c.getContact_name())) {// matches names
                    contactID = c.getContact_id();// assigns the name with it's id
                }
            }
            for (Appointment appt : getAllApptInfo) {// scans through appointment list
                if (appt.getContact_id() == contactID) {// gets a contact id from each appointment and checks
                    // to see if it matches the contact id  from the name selected above
                    apptContact = appt;
                    apptData.add(apptContact);// adds a customer's appointment upon a valid match
                }
            }
            contactSchedulesTableView.setItems(apptData);// display appointments

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Initializes the reports with data based from the database
     * Lambda #11 gets contact name from each contact object
     * Lambda #12 gets the type from each appointment object
     * Lambda #13 gets the starting month from each appointment object and adds it the apptMonth list
     * Lambda #14 filters through the list, if the month has not been already added, adds it to list
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // initialize the contact box (list all contact names to select from)
        ObservableList<Contact> allContacts = ContactDAO.getContacts();
        ObservableList<String> allContactNames = FXCollections.observableArrayList();
        // lambda #11
        allContacts.forEach(c -> allContactNames.add(c.getContact_name()));
        contactCBox.setItems(allContactNames);

        // initialize set up for table data [Report #1]
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        // Report # 2
        ObservableList<Appointment> getAllAppts = AppointmentDAO.getAppointments();
        ObservableList<Month> apptMonth = FXCollections.observableArrayList();// list holds all months
        ObservableList<Month> monthOfAppt = FXCollections.observableArrayList();// holds the month connected to a appt

        ObservableList<String> apptType = FXCollections.observableArrayList();// records type from each appt
        ObservableList<String> diffApptType = FXCollections.observableArrayList(); // holds only unique appts.

        ObservableList<ReportByType> repType = FXCollections.observableArrayList(); // by Type
        ObservableList<ReportByMonth> repMonths = FXCollections.observableArrayList(); // hold months

        // gets the type from each appointment
        getAllAppts.forEach(appt -> apptType.add(appt.getType())); // lambda #12
        // goes through the list of appts, for each appt element of stream,
        // gets the starting month and adds it the apptMonth list
        getAllAppts.stream().map(appt -> appt.getStart().getMonth()).forEach(apptMonth::add);//lambda #13
        // filters through the list, if the month has not been already added, add it to list
        apptMonth.stream().filter(month -> !monthOfAppt.contains(month)).forEach(monthOfAppt::add);// lambda #14

        // goes through list of types returned from the appts,
        // will only add unique/different types to list (no repeats)
        for(Appointment appts: getAllAppts){
            String type = appts.getType(); // gets appt type
            if(!diffApptType.contains(type)){
                diffApptType.add(type); // only adds the type if it's not already in diffApptType list
            }
        }
        // Collections.frequency: counts the number of same elements in a list
        for(Month month : monthOfAppt){
            int total = Collections.frequency(apptMonth, month);
            String nameOfMonth = month.name();
            ReportByMonth m = new ReportByMonth(nameOfMonth,total);
            repMonths.add(m);
        }

        for(String type: diffApptType){
            String t = type;
            int total = Collections.frequency(apptType, t);
            ReportByType types = new ReportByType(t,total);
            repType.add(types);
        }

        // total appts by Month table
        monthTotalApptTableView.setItems(repMonths);
        monthColR2.setCellValueFactory(new PropertyValueFactory<>("apptMonth"));
        totalMonthR2.setCellValueFactory(new PropertyValueFactory<>("apptTotal"));
        // total appts by type table
        typeTotalApptTableView.setItems(repType);
        typeColR2.setCellValueFactory( new PropertyValueFactory<>("apptType"));
        totalTypeR2.setCellValueFactory(new PropertyValueFactory<>("apptTotal"));


        // Report # 3
        ObservableList<ReportCountry> groupCountries = null;
        try {
            groupCountries = ReportDAO.getCountries();
            // the tallies from countries are put into a separate list
            ObservableList<ReportCountry> addCountry = FXCollections.observableArrayList();

            groupCountries.forEach(addCountry::add);// adds the total by each country
            countryTotalTableView.setItems(addCountry);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // note the name is based the constructor of the class
        countryColR3.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        totalR3.setCellValueFactory(new PropertyValueFactory<>("countCountry"));

    }
}
