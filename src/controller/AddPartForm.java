package controller;

import classes.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Isaac Zamudio
 */

/**
 * FUTURE ENHANCEMENT: One important improvement it can be done in this class is to add a method that call the
 *      toMain section, this because currently when you need to go bac to main I created the entire code to go to
 *      main in every part, this make extra code that is not needed.
 *      Another part can be to create a specific class where all the alarms, error and windows are. This
 *      can help reduce the code, make easier to add more error into the class and implement a switch structure
 *      to facilitate to read the code
 *      One last part could be to add a window with the mistakes in the form, instead of a label, so the user can
 *      pay more attention to them, and the user clicks ok, it can be helpful to empty the filed. This can improve
 *      the experience of the user but also avoid more confusion (color to the borders could be added to simplify
 *      more)
 */
public class AddPartForm implements Initializable {

    public TextField TFpartName;
    public TextField TFpartInv;
    public TextField TFpartPrice;
    public TextField TFmaxPart;
    public TextField TFminPart;
    public TextField TFpartId;
    public ToggleGroup tpartgroup;
    public Label MachID_CompanyLabel;
    public RadioButton radOutsourced;
    public RadioButton radInHouse;
    public int IDgenerated;
    public boolean errorBool = false;
    String errorMessage;
    public TextField TFMachID_CompName;
    public Label errorLabel;
    public ObservableList<Part> parts = Inventory.getAllParts();


    /**
     * This toMain action event button takes you back to the mainForm
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This initialize the Form
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * This onInHouse action event radio button sets the MachID_CompanyLabel to MachineID
     */
    public void onInHouse(ActionEvent actionEvent) throws IOException {
        MachID_CompanyLabel.setText("Machine ID");
    }

    /**
     * This onOutsourced action event radio button sets the MachID_CompanyLabel to Company Name
     */
    public void onOutsourced(ActionEvent actionEvent) throws IOException{
        MachID_CompanyLabel.setText("Company Name");
    }

    /**
     * This savePart action event saves the Part into the allParts list, but first it verifies the information
     * RUNTIME ERROR:  This error was cause by the fact that even if an errors were found on the information provided
     *      on the fields, the information still tried to save them making the program to send errors.
     *      The solution I found was to add a boolean condition, so if an error was found, the program didn't tried to
     *      save it and wait for the user to adjust the mistakes on the fields
     */
    public void savePart(ActionEvent actionEvent) throws IOException{
        errorBool = false;

        if(radInHouse.isSelected()){
            verifyingInHouse();
            if(errorBool == false) {
                Inventory.addPart(new InHouse(generateID(), TFpartName.getText().trim(),
                        Double.parseDouble(TFpartPrice.getText().trim()), Integer.parseInt(TFpartInv.getText().trim()),
                        Integer.parseInt(TFminPart.getText().trim()), Integer.parseInt(TFmaxPart.getText().trim()),
                        Integer.parseInt(TFMachID_CompName.getText().trim())));
            }
        }
        else{
            verifyingOutsourced();
            if(errorBool == false) {
                Inventory.addPart(new Outsourced(generateID(), TFpartName.getText().trim(),
                        Double.parseDouble(TFpartPrice.getText().trim()), Integer.parseInt(TFpartInv.getText().trim()),
                        Integer.parseInt(TFminPart.getText().trim()), Integer.parseInt(TFmaxPart.getText().trim()),
                        TFMachID_CompName.getText().trim()));
            }
        }

        if(errorBool == false) {

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This verifyingInformation method verifies the main information
     * RUNTIME ERROR: This error was cause by not having enough conditions when checking the information this cause
     *      an error to occur since there was not instruction on what to do when a condition wasn't meet.
     *      The solution I found was using try{} catch{} structure so after checking every aspect on the data
     *      provided the catch part will display what is need it, this discarded the runtime error occur which was cause
     *      several times in different text fields
     */
    public void verifyingInformation(){

/////////////////------This just makes sure the name Field is not left empty------////////////////////////////
        errorMessage = "";
        if (TFpartName.getText().trim().isEmpty()){
            errorBool = true;
            errorMessage = errorMessage + "\nThe name is empty";}

////////////------This section checks the min field to make sure is not empty and that its an integer---//////////
        boolean minEmpty = false;
        boolean minError = false;
        try {
            if (TFminPart.getText().trim().isEmpty()){
                minEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMin cannot be empty";}
            else if (Integer.parseInt(TFminPart.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nMin cannot be negative";}
            }catch (Exception a){
                errorBool = true;
                minError = true;
                errorMessage = errorMessage + "\nMin is not an integer";
            }

///////////--------This section checks the min field to make sure is not empty and that its an integer----////////
        boolean maxEmpty = false;
        boolean maxError = false;
        try{
            if (TFmaxPart.getText().trim().isEmpty()){
                maxEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMax cannot be empty";}
            else if (Integer.parseInt(TFmaxPart.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nMax cannot be negative";}
        }catch (Exception b){
            errorBool = true;
            maxError = true;
            errorMessage = errorMessage + "\nMax is not an Integer";
        }
/////---If there is nothing wrong with min and max then this section check that min is never more than the max---///
        if(!minEmpty && !maxEmpty) {
            if(!minError && !maxError) {
                if (Integer.parseInt(TFminPart.getText().trim()) > Integer.parseInt(TFmaxPart.getText().trim())) {
                    errorBool = true;
                    errorMessage = errorMessage + "\nMin cannot be bigger than Max";
                }
            }
        }

////////////////////--- This section makes sure the number is a integer or double------//////////////////////////////
        try {
            if (TFpartPrice.getText().trim().isEmpty()) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be empty"; }
            else if (Double.parseDouble(TFpartPrice.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be negative"; }
        }catch (Exception c){
            errorBool = true;
            errorMessage = errorMessage + "\nThe Price is not double";
        }

////////////-----This section makes sure the inventory is an integer and that the field is not---/////////
// ////////------empty, as well as checking withing rage-----/////////////////////////////////////////////
        try{
            if (TFpartInv.getText().trim().isEmpty()){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory is empty";
            }
            else if (Integer.parseInt(TFpartInv.getText().trim()) < 0){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory cannot be negative";
            }
            else {
                if (!minError && !minEmpty) {
                    if (Integer.parseInt(TFpartInv.getText().trim()) < Integer.parseInt(TFminPart.getText().trim())) {
                        errorBool = true;
                        errorMessage = errorMessage + "\nThe inventory cannot be below the Min";
                    }
                }
                if (!maxError && !maxEmpty) {
                    if (Integer.parseInt(TFpartInv.getText().trim()) > Integer.parseInt(TFmaxPart.getText().trim())) {
                        errorBool = true;
                        errorMessage = errorMessage + "\nThe inventory cannot be above the Max";
                    }
                }
            }

        }catch (Exception d) {
            errorBool = true;
            errorMessage = errorMessage + "\nThe Inventory is not an Integer";
        }
////////////////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * This verifyingInHouse method verifies information for InHouse Parts
     * RUNTIME ERROR: This was not cause a program issue but rather allowing the error label to display an empty
     *      exception label with no mistakes.
     *      The solution was just to add a boolean condition.
     */
    public void verifyingInHouse() {
        verifyingInformation();
        try {
            if (TFMachID_CompName.getText().trim().isEmpty()) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Machine ID is not Integer";
            }
            else if (Integer.parseInt(TFMachID_CompName.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Machine ID cannot be negative";}

        }catch (Exception e) {
            errorBool = true;
            errorMessage = errorMessage + "\nThe Machine ID is not an Integer";
        }
        if (errorBool) {errorLabel.setText("Exception: " + errorMessage);}
    }

    /**
     * This verifyingOutsourced method verifies information for Outsourced Parts
     * RUNTIME ERROR: This was not cause a program issue but rather allowing the error label to display an empty
     *      exception label with no mistakes.
     *      The solution was just to add a boolean condition.
     */
    public void verifyingOutsourced() {
        verifyingInformation();
        if (TFMachID_CompName.getText().trim().isEmpty()) {
            errorBool = true;
            errorMessage = errorMessage + "\nThe company Name cannot be empty";
        }
        if(errorBool) {errorLabel.setText("Exception: " + errorMessage);}
    }

    /**
     * This generateID method generates the ID's for the Parts
     */
    public int generateID() {
        for(Part p : parts)
            IDgenerated = p.getId();

        return IDgenerated + 1;
    }
}
