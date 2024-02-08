package controller;

import classes.*;
import javafx.application.Platform;
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

import static controller.ModifyPartForm.modifyPart;
import static controller.ModifyPartForm.setPartIndex;
import static controller.ModifyProductForm.modifyProduct;
import static controller.ModifyProductForm.setProductIndex;

/**
 * @author Isaac Zamudio
 */
public class MainForm implements Initializable {
    public TableColumn partsPartIdCol;
    public TableColumn partsNameCol;
    public TableColumn partsInvCol;
    public TableColumn partsPriceCol;
    public TableColumn prodIdCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public TableView<Part> partsTable;
    public TableView<Product> productsTable;
    public TextField PartByIDorName;
    public TextField ProdByIDorName;

    public ObservableList<Product> products = Inventory.getAllProduct();
    public ObservableList<Part> parts = Inventory.getAllParts();

    /**
     * This toAddPart button action opens the AddPartForm
     */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This toModifyPart button action takes take the selected Part from the productsTable
     * send it to the the ModifyPartForm and that form is open wth all of the information.
     * If no Part is selected a window will open telling you to select one.
     */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {

        Part partSelected = partsTable.getSelectionModel().getSelectedItem();
        if(!(partSelected == null)) {
            modifyPart(partSelected);
            setPartIndex(parts.indexOf(partSelected));

            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPartForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 700, 600);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
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
     * This toAddProduct button action opens the AddPartForm
     */
    public void toAddProduct(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This toAddProduct button action takes take the selected Product from the productsTable
     * send it to the the ModifyProductForm and that form is open wth all of the information.
     * If no Product is selected a window will open telling you to select one.
     */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException{
        Product productSelected = productsTable.getSelectionModel().getSelectedItem();
        if(!(productSelected == null)) {
            modifyProduct(productSelected);
            setProductIndex(products.indexOf(productSelected));

            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProductForm.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 650);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
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
     * This initialize the program with information to the tables displayed in the MainForm
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProduct());

        partsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This searchProdByIDorName text field action event is use to search the Product by name or ID  in the partsTable
     */
    public void searchProdByIDorName(ActionEvent actionEvent) {
        String q = ProdByIDorName.getText();
        products = Inventory.lookupProduct(q);

        if(products.size() == 0){
            try {
                int id = Integer.parseInt(q);
                Product p = Inventory.lookupProduct(id);
                if (p != null) {
                    products.add(p);
                }
            }
            catch (NumberFormatException e)
            {
                //ignore
            }
        }

        productsTable.setItems(products);
        ProdByIDorName.setText("");
    }

    /**
     * This searchPartByIDorName text field action event is use to search the Part by name or ID  in the partsTable
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

        partsTable.setItems(parts);
        PartByIDorName.setText("");
    }

    /**
     * This deletingPart button action event is use to delete the Part selected in the partsTable, if no part is
     * selected and window will open asking you to select one part before clicking on the button
     */
    public void deletingPart(ActionEvent actionEvent) {

        Part removingPart = partsTable.getSelectionModel().getSelectedItem();
        if(!( removingPart == null)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete part");
            alert.setHeaderText("Are you sure you want to delete: " + removingPart.getName());
            alert.setContentText("Click ok to confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                boolean deleted = false;
                deleted = Inventory.deletePart(removingPart);
                parts.remove(removingPart);
            }
            else{ return;}
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error deleting part");
            alert.setHeaderText("Cannot delete part");
            alert.setContentText("You have  not selected a Part");
            alert.showAndWait();
        }
    }

    /**
     * This deletingProduct button action event is use to delete the Product selected in the partsTable, if no part is
     * selected and window will open asking you to select one part before clicking on the button
     */
    public void deletingProduct(ActionEvent actionEvent) {

        Product removingProduct = productsTable.getSelectionModel().getSelectedItem();
        if(!(removingProduct == null)) {
            ObservableList<Part> assocParts = removingProduct.getAllAssociatedParts();
            if(assocParts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete part");
                alert.setHeaderText("Are you sure you want to delete: " + removingProduct.getName());
                alert.setContentText("Click ok to confirm");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    boolean deleted = false;
                    deleted = Inventory.deleteProduct(removingProduct);
                    products.remove(removingProduct);
                }
                else {return;}
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error deleting product");
                alert.setHeaderText("Cannot delete product");
                alert.setContentText("This Product has some associated Parts");
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error deleting product");
            alert.setHeaderText("Cannot delete product");
            alert.setContentText("You have  not selected a Product");
            alert.showAndWait();
        }
    }

    /**
     * This exitProgram button action terminates the program and close it
     */
    public void exitProgram(ActionEvent actionEvent) {
        Platform.exit();
    }
}
