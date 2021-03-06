package application;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.*;
//import application.OurTiffReader.*;
//import mil.nga.tiff.TiffReader;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    /**
     * The data as an observable list of Persons.
     */
//    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSW-Coverage-Radar");
        initRootLayout();
    }



    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/MainPage.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            MainPageOverviewController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showConfigurationDialog(RadarParameters radarParameters) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource("view/RadarEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Radar");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            RadarEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRadar(radarParameters);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
//            return false;
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
//    /**
//     * Returns the person file preference, i.e. the file that was last opened.
//     * The preference is read from the OS specific registry. If no such
//     * preference can be found, null is returned.
//     *
//     * @return
//     */
//    public File getPersonFilePath() {
//        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
//        String filePath = prefs.get("filePath", null);
//        if (filePath != null) {
//            return new File(filePath);
//        } else {
//            return null;
//        }
//    }

//    /**
//     * Sets the file path of the currently loaded file. The path is persisted in
//     * the OS specific registry.
//     *
//     * @param file the file or null to remove the path
//     */
//    public void setPersonFilePath(File file) {
//        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
//        if (file != null) {
//            prefs.put("filePath", file.getPath());
//
//            // Update the stage title.
////            primaryStage.setTitle("AddressApp - " + file.getName());
//        } else {
//            prefs.remove("filePath");
//
//            // Update the stage title.
////            primaryStage.setTitle("AddressApp");
//        }
//    }
    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
//    public void loadPersonDataFromFile(File file) {
//        try {
//            JAXBContext context = JAXBContext
//                    .newInstance(PersonListWrapper.class);
//            Unmarshaller um = context.createUnmarshaller();
//
//            // Reading XML from the file and unmarshalling.
//            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);
//
//            personData.clear();
//            personData.addAll(wrapper.getPersons());
//
//            // Save the file path to the registry.
//            setPersonFilePath(file);
//
//        } catch (Exception e) { // catches ANY exception
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Could not load data");
//            alert.setContentText("Could not load data from file:\n" + file.getPath());
//
//            alert.showAndWait();
//        }
//    }
//
//    /**
//     * Saves the current person data to the specified file.
//     *
//     * @param file
//     */
//    public void savePersonDataToFile(File file) {
//        try {
//            JAXBContext context = JAXBContext
//                    .newInstance(PersonListWrapper.class);
//            Marshaller m = context.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            // Wrapping our person data.
//            PersonListWrapper wrapper = new PersonListWrapper();
//            wrapper.setPersons(personData);
//
//            // Marshalling and saving XML to the file.
//            m.marshal(wrapper, file);
//
//            // Save the file path to the registry.
//            setPersonFilePath(file);
//        } catch (Exception e) { // catches ANY exception
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Could not save data");
//            alert.setContentText("Could not save data to file:\n" + file.getPath());
//
//            alert.showAndWait();
//        }
//    }
}