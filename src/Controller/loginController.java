package Controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class displays the login screen
 */
public class loginController implements Initializable {

    Stage stage;
    Parent scence;

    @FXML
    private TextField passwordInputTXT;
    @FXML
    private Label passwordTxt;

    @FXML
    private TextField usernameInputTXT;
    @FXML
    private Label userNametxt;
    @FXML
    private Button loginBtn;
    @FXML
    private Label loginTitletxt;
    @FXML
    private Label userLocationLabel;
    @FXML
    private Label locationTxtLabel;

    /**
     * Login button, user will go through a validation process.
     * If passed, takes user to main navigation menu
     * @param event clicking on the Login button
     * @throws IOException
     */
    public void onActionLogin(ActionEvent event) throws IOException {
        // creates a new txt file to record login attempts
        FileWriter file = new FileWriter("login_activity.txt",true);

        ObservableList<Appointment> getAllAppts = AppointmentDAO.getAppointments();

        LocalDateTime presentTime = LocalDateTime.now(); // grabs right this moment time
        LocalDateTime currTimePlus15 = LocalDateTime.now().plusMinutes(15);// used for 15 min appointment alert
        LocalDateTime start;
        // initialize variables
        int getApptID = 0;
        LocalDateTime displayTime = null;
        boolean apptWithin15 = false;

        try{
            // Used to translate to language
             ResourceBundle rb = ResourceBundle.getBundle("main/Nat", Locale.getDefault());

            // gets what the user inputted
            String inputUserName = usernameInputTXT.getText();
            String inputPassword = passwordInputTXT.getText();

            int validation = UserDAO.validateUser(inputUserName, inputPassword);

            if(validation > 0){
                // note successful login
                file.write("Username: " + inputUserName +
                        " Password: " + inputPassword +
                        " Logged in successfully at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                file.close();

                // if the user enters the correct input, goes to main menu
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scence = FXMLLoader.load(getClass().getResource("/Views/mainMenu.fxml"));
                stage.setScene(new Scene(scence));
                stage.show();

                // See if there are any upcoming appts.
                for(Appointment appt:getAllAppts){
                    start = appt.getStart();// retrieve the start time
                    if((start.isAfter(presentTime) && (start.isBefore(currTimePlus15)))){
                        getApptID = appt.getAppointment_id();
                        displayTime = start;// display time holds the time
                        apptWithin15 = true;
                    }
                }
                if(apptWithin15 != false){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Appointment Notice");
                    alert.setContentText("Appt. ID: " + getApptID + " has an appointment at " + displayTime);
                    alert.showAndWait();
                }else{
                    Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Appointment Notice");
                    alert.setContentText("There are no appointments within 15 minutes.");
                    alert.showAndWait();
                }
            }else if(validation < 0){
                // note unsuccessful login attempt
                file.write("Username: " + inputUserName +
                        " Password: " + inputPassword +
                        " Failed to login successfully at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
                file.close();

                // wrong username/password inputted alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Incorrect"));
                alert.setContentText(rb.getString("Error"));
                alert.showAndWait();
            }

        }catch (IOException io){
          io.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            // display the location of the current user
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

            ZoneId location = ZoneId.systemDefault();
            // displays time based on time zone
            userLocationLabel.setText(String.valueOf(location));

            // Initializing the text to the desired language of the login screen
            ResourceBundle rb = ResourceBundle.getBundle("main/Nat", Locale.getDefault());
            loginTitletxt.setText(rb.getString("Login"));
            locationTxtLabel.setText(rb.getString("Location"));
            userNametxt.setText(rb.getString("Username"));
            passwordTxt.setText(rb.getString("Password"));
            loginBtn.setText(rb.getString("Login"));

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
