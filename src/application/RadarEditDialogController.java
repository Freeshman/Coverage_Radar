package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.Person;
import application.DateUtil;

/**
 * Dialog to edit details of a person.
 *
 * @author Marco Jakob
 */
public class RadarEditDialogController {

    @FXML
    private TextField Long;
    @FXML
    private TextField Lati;
    @FXML
    private TextField Height;
    @FXML
    private TextField Til;
    @FXML
    private TextField Range;


    private Stage dialogStage;
    private RadarParameters radar;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param person
     */
    public void setRadar(RadarParameters radar) {
        this.radar  = radar;
        Long.setText(String.valueOf(radar.GetL()));
        Lati.setText(String.valueOf(radar.GetB()));
        Height.setText(String.valueOf(radar.GetH()));
        Til.setText(String.valueOf(radar.GetTil()));
        Range.setText(String.valueOf((int) (radar.GetMaxRange()/1000.0)));
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	radar.SetL(Double.valueOf(Long.getText()).doubleValue());
			radar.SetB(Double.valueOf(Lati.getText()).doubleValue());
        	radar.SetH(Double.valueOf(Height.getText()).doubleValue());
			radar.SetMaxRange(Double.valueOf(Range.getText()).doubleValue()*1000);
			radar.SetTil(Double.valueOf(Til.getText()).doubleValue());
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (Long.getText() == null || Long.getText().length() == 0) {
            errorMessage += "No valid longitude!\n";
        }
        if (Lati.getText() == null || Lati.getText().length() == 0) {
            errorMessage += "No valid latitude!\n";
        }
        if (Height.getText() == null || Height.getText().length() == 0) {
            errorMessage += "No valid height!\n";
        }

        if (Til.getText() == null || Til.getText().length() == 0) {
            errorMessage += "No valid til!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(Range.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid max range (must be an integer)!\n";
            }
        }



        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}