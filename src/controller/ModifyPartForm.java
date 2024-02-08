package controller;

import classes.InHouse;
import classes.Inventory;
import classes.Outsourced;
import classes.Part;
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
public class ModifyPartForm implements Initializable {

    public RadioButton radInHouse;
    public ToggleGroup tpartgroup;
    public RadioButton radOutsourced;
    public TextField partIdTF;
    public TextField partNameTF;
    public TextField partInvTF;
    public TextField partPriceTF;
    public TextField partMaxTF;
    public TextField machineId_CompanyTF;
    public TextField partMinTF;
    public Label MachID_CompanyLabel;
    public static Part part;
    public static int partIndex;
    public boolean errorBool = false;
    public Label errorLabel;
    String errorMessage;

    /**
     * This sets the parameter part with the partSelected
     */
    public static void modifyPart(Part partSelected) {
        part = partSelected;
    }

    /**
     * This sets the index of the partSelected
     */
    public static void setPartIndex(int index){
        partIndex = index;
    }

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
     * This initialized the form depending on the radio button that you select.
     * The change is made on the Machine ID or Company name, depending on the InHouse or Outsourced ratio button
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (part instanceof InHouse ) {

            InHouse partInHouse = (InHouse) part;
            radInHouse.setSelected(true);
            MachID_CompanyLabel.setText("Machine ID");
            partNameTF.setText(partInHouse.getName());
            partIdTF.setText((Integer.toString(partInHouse.getId())));
            partInvTF.setText((Integer.toString(partInHouse.getStock())));
            partPriceTF.setText((Double.toString(partInHouse.getPrice())));
            partMinTF.setText((Integer.toString(partInHouse.getMin())));
            partMaxTF.setText((Integer.toString(partInHouse.getMax())));
            machineId_CompanyTF.setText((Integer.toString(partInHouse.getMachineId())));

        }

        if (part instanceof Outsourced) {

            Outsourced partOutsourced = (Outsourced) part;
            radOutsourced.setSelected(true);
            MachID_CompanyLabel.setText("Company Name");
            partNameTF.setText(partOutsourced.getName());
            partIdTF.setText((Integer.toString(partOutsourced.getId())));
            partInvTF.setText((Integer.toString(partOutsourced.getStock())));
            partPriceTF.setText((Double.toString(partOutsourced.getPrice())));
            partMinTF.setText((Integer.toString(partOutsourced.getMin())));
            partMaxTF.setText((Integer.toString(partOutsourced.getMax())));
            machineId_CompanyTF.setText(partOutsourced.getCompanyName());
        }

    }

    /**
     * This onInHouse action event radio button sets the MachID_CompanyLabel to MachineID
     */
    public void onInHouse(ActionEvent actionEvent) {
        MachID_CompanyLabel.setText("Machine ID");
    }

    /**
     * This onOutsourced action event radio button sets the MachID_CompanyLabel to Company Name
     */
    public void onOutsourced(ActionEvent actionEvent) {
        MachID_CompanyLabel.setText("Company Name");
    }

    /**
     * This savingPartChanges action event saves the changes you made to the selected Part into the allParts list;
     * but it verifies the information first
     * RUNTIME ERROR:  This error was cause by the fact that even if an errors were found on the information provided
     *      on the fields, the information still tried to save them making the program to send errors.
     *      The solution I found was to add a boolean condition, so if an error was found, the program didn't tried to
     *      save it and wait for the user to adjust the mistakes on the fields
     */
    public void savingPartChanges(ActionEvent actionEvent) throws IOException {
        errorBool = false;

        if(radInHouse.isSelected()){
            verifyingInHouse();
            if(errorBool == false) {
                Part changedPart = new InHouse(part.getId(), partNameTF.getText().trim(),
                        Double.parseDouble(partPriceTF.getText().trim()), Integer.parseInt(partInvTF.getText().trim()),
                        Integer.parseInt(partMinTF.getText().trim()), Integer.parseInt(partMaxTF.getText().trim()),
                        Integer.parseInt(machineId_CompanyTF.getText().trim()));
                Inventory.updatePart(partIndex, changedPart);
            }
        }

        if(radOutsourced.isSelected()){
            verifyingOutsourced();
            if(errorBool == false) {
                Part changedPart = new Outsourced(part.getId(), partNameTF.getText().trim(),
                        Double.parseDouble(partPriceTF.getText().trim()), Integer.parseInt(partInvTF.getText().trim()),
                        Integer.parseInt(partMinTF.getText().trim()), Integer.parseInt(partMaxTF.getText().trim()),
                        machineId_CompanyTF.getText().trim());
                Inventory.updatePart(partIndex, changedPart);
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
     *       an error to occur since there was not instruction on what to do when a condition wasn't meet.
     *       The solution I found was using try{} catch{} structure so after checking every aspect on the data
     *       provided the catch part will display what is need it, this discarded the runtime error occur which was cause
     *       several times in different text fields
     */
    public void verifyingInformation(){

/////////////////------This just makes sure the name Field is not left empty------////////////////////////////
        errorMessage = "";
        if (partNameTF.getText().trim().isEmpty()){
            errorBool = true;
            errorMessage = errorMessage + "\nThe name is empty";}

////////////------This section checks the min field to make sure is not empty and that its an integer---//////////
        boolean minEmpty = false;
        boolean minError = false;
        try {
            if (partMinTF.getText().trim().isEmpty()){
                minEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMin cannot be empty";}
            else if (Integer.parseInt(partMinTF.getText().trim()) < 0) {
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
            if (partMaxTF.getText().trim().isEmpty()){
                maxEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMax cannot be empty";}
            else if (Integer.parseInt(partMaxTF.getText().trim()) < 0) {
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
                if (Integer.parseInt(partMinTF.getText().trim()) > Integer.parseInt(partMaxTF.getText().trim())) {
                    errorBool = true;
                    errorMessage = errorMessage + "\nMin cannot be bigger than Max";
                }
            }
        }

////////////////////--- This section makes sure the number is a integer or double------//////////////////////////////
        try {
            if (partPriceTF.getText().trim().isEmpty()) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be empty"; }
            else if (Double.parseDouble(partPriceTF.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be negative"; }
        }catch (Exception c){
            errorBool = true;
            errorMessage = errorMessage + "\nThe Price is not double";
        }

////////////-----This section makes sure the inventory is an integer and that the field is not---/////////
// ////////------empty, as well as checking withing rage-----/////////////////////////////////////////////
        try{
            if (partInvTF.getText().trim().isEmpty()){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory is empty";
            }
            else if (Integer.parseInt(partInvTF.getText().trim()) < 0){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory cannot be negative";
            }
            else {
                if (!minError && !minEmpty) {
                    if (Integer.parseInt(partInvTF.getText().trim()) < Integer.parseInt(partMinTF.getText().trim())) {
                        errorBool = true;
                        errorMessage = errorMessage + "\nThe inventory cannot be below the Min";
                    }
                }
                if (!maxError && !maxEmpty) {
                    if (Integer.parseInt(partInvTF.getText().trim()) > Integer.parseInt(partMaxTF.getText().trim())) {
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
            if (machineId_CompanyTF.getText().trim().isEmpty()) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Machine ID is not Integer";
            }
            else if (Integer.parseInt(machineId_CompanyTF.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Machine ID cannot be negative";}

        }catch (Exception e) {
            errorBool = true;
            errorMessage = errorMessage + "\nThe Machine ID is not an Integer";
        }
        if(errorBool) {errorLabel.setText("Exception: " + errorMessage);}
    }

    /**
     * This verifyingOutsourced method verifies information for Outsourced Parts
     * RUNTIME ERROR: This was not cause a program issue but rather allowing the error label to display an empty
     *      exception label with no mistakes.
     *      The solution was just to add a boolean condition.
     */
    public void verifyingOutsourced() {
        verifyingInformation();
        if (machineId_CompanyTF.getText().trim().isEmpty()) {
            errorBool = true;
            errorMessage = errorMessage + "\nThe company Name is not String";
        }
        if(errorBool) {errorLabel.setText("Exception: " + errorMessage);}
    }
}
