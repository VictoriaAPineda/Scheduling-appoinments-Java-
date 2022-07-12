package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class displays the main navigation menu
 */
public class mainMenuController implements Initializable {
    Stage stage;
    Parent scence;

    /**
     * Reports button, takes user to Reports view
     * @param actionEvent clicks on Report button
     * @throws IOException
     */
    public void onActionGoToReports(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/reports.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Customer Appointments button, takes user to customer schedules view
     * @param actionEvent clicks on Customer Appointments button
     * @throws IOException
     */
    public void onActionGoToCustAppts(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerSchedules.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    /**
     * Customer Records button, takes user to customer records view
     * @param actionEvent clicks on Customer Records button
     * @throws IOException
     */
    public void onActionGoToCustRec(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scence = FXMLLoader.load(getClass().getResource("/Views/customerRecords.fxml"));
        stage.setScene(new Scene(scence));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
