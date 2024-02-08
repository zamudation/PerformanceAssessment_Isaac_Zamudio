package controller;

import classes.Inventory;
import classes.Part;
import classes.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
public class AddProductForm implements Initializable {

    public TableView partsSearchTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn inventoryCol;
    public TableColumn priceCol;
    public TableView aPartsTable;
    public TableColumn aPartIDCol;
    public TableColumn aPartNameCol;
    public TableColumn aInventoryCol;
    public TableColumn aPriceCol;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField invTextField;
    public TextField priceTextField;
    public TextField maxTextField;
    public TextField minTextField;
    public TextField PartByIDorName;
    public Label errorLabel;
    String errorMessage = "";
    public int IDgenerated;
    public boolean errorBool = false;

    private ObservableList<Product> products = Inventory.getAllProduct();
    private ObservableList<Part> parts = FXCollections.observableArrayList();
    private ObservableList<Part> aPartList = FXCollections.observableArrayList();

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
     * This initialize the Form by setting up the partsSearchTable with all the parts in inventory to be added
     * if we want to
     * It also initialize aPartsTable to empty, ready to be use
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsSearchTable.setItems(Inventory.getAllParts());
        aPartsTable.setItems(aPartList);

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        aPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This searchPartByIDorName Text Field action event lets you search the Part by Name or ID
     */
    public void searchPartByIDorName(ActionEvent actionEvent) {
        String q = PartByIDorName.getText();
            parts = Inventory.lookupPart(q);

        if(parts.size() == 0){
            try {
                int id = Integer.parseInt(q);
                Part p = Inventory.lookupPart(id);
                if (p != null) {
                    parts.add(p);
                }
            }
            catch (NumberFormatException e)
            {
                //ignore
            }
        }

        partsSearchTable.setItems(parts);
        PartByIDorName.setText("");
    }

    /**
     * This saveProduct action event saves the Product into the allParts list, but first
     * it verifies the information
     * RUNTIME ERROR:  This error was cause by the fact that even if an errors were found on the information provided
     *      on the fields, the information still tried to save them making the program to send errors.
     *      The solution I found was to add a boolean condition, so if an error was found, the program didn't tried to
     *      save it and wait for the user to adjust the mistakes on the fields
     */
    public void saveProduct(ActionEvent actionEvent) throws IOException {
        errorBool = false;

        verifyingInformation();
        if (errorBool) {errorLabel.setText("Exception: " + errorMessage);}

        if (!errorBool) {
            Product newProduct = new Product(generateID(), nameTextField.getText().trim(),
                    Double.parseDouble(priceTextField.getText().trim()), Integer.parseInt(invTextField.getText().trim()),
                    Integer.parseInt(minTextField.getText().trim()), Integer.parseInt(maxTextField.getText().trim()));

            for (Part p : aPartList) {
                newProduct.addAssociatedPart(p);
            }

            Inventory.addProduct(newProduct);


            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 400);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This addingAssocPart action event button lets you add associated Parts to the Product if you want to
     */
    public void addingAssocPart(ActionEvent actionEvent) {
        Part addAssocPart = (Part) partsSearchTable.getSelectionModel().getSelectedItem();
        if(!(addAssocPart == null)){
            aPartList.add(addAssocPart);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item!");
            alert.showAndWait();
        }
    }

    /**
     * This removingAssocPart action event button lets you remove associated Parts to the Product if you want to
     */
    public void removingAssocPart(ActionEvent actionEvent) {
        Part removingPart = (Part) aPartsTable.getSelectionModel().getSelectedItem();

        if (!( removingPart == null)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete part");
            alert.setHeaderText("Are you sure you want to delete: " + removingPart.getName());
            alert.setContentText("Click ok to confirm");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {
                aPartList.remove(aPartsTable.getSelectionModel().getSelectedItem());
            }
            else{
                return;
            }
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

////////////////////------This just makes sure the name Field is not left empty------////////////////////////////
        errorMessage = "";
        if (nameTextField.getText().trim().isEmpty()){
            errorBool = true;
            errorMessage = errorMessage + "\nThe name is empty";}

////////////------This section checks the min field to make sure is not empty and that its an integer---//////////
        boolean minEmpty = false;
        boolean minError = false;
        try {
            if (minTextField.getText().trim().isEmpty()){
                minEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMin cannot be empty";}
            else if (Integer.parseInt(minTextField.getText().trim()) < 0) {
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
            if (maxTextField.getText().trim().isEmpty()){
                maxEmpty = true;
                errorBool = true;
                errorMessage = errorMessage + "\nMax cannot be empty";}
            else if (Integer.parseInt(maxTextField.getText().trim()) < 0) {
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
                if (Integer.parseInt(minTextField.getText().trim()) > Integer.parseInt(maxTextField.getText().trim())) {
                    errorBool = true;
                    errorMessage = errorMessage + "\nMin cannot be bigger than Max";
                }
            }
        }

////////////////////--- This section makes sure the number is a integer or double------//////////////////////////////
        try {
            if (priceTextField.getText().trim().isEmpty()) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be empty"; }
            else if (Double.parseDouble(priceTextField.getText().trim()) < 0) {
                errorBool = true;
                errorMessage = errorMessage + "\nThe Price cannot be negative"; }
        }catch (Exception c){
            errorBool = true;
            errorMessage = errorMessage + "\nThe Price is not double";
        }

////////////-----This section makes sure the inventory is an integer and that the field is not---/////////
// ////////------empty, as well as checking withing rage-----/////////////////////////////////////////////
        try{
            if (invTextField.getText().trim().isEmpty()){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory is empty";
            }
            else if (Integer.parseInt(invTextField.getText().trim()) < 0){
                errorBool = true;
                errorMessage = errorMessage + "\nInventory cannot be negative";
            }
            else {
                if (!minError && !minEmpty) {
                    if (Integer.parseInt(invTextField.getText().trim()) < Integer.parseInt(minTextField.getText().trim())) {
                        errorBool = true;
                        errorMessage = errorMessage + "\nThe inventory cannot be below the Min";
                    }
                }
                if (!maxError && !maxEmpty) {
                    if (Integer.parseInt(invTextField.getText().trim()) > Integer.parseInt(maxTextField.getText().trim())) {
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
     * This generateID method generates the ID's for the Parts
     */
    public int generateID() {
        for(Product p : products)
            IDgenerated = p.getId();

        return IDgenerated + 1;
    }
}
