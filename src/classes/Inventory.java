package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Isaac Zamudio
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProduct = FXCollections.observableArrayList();

    /**
     * @param newPart the newPart is added into the allParts ObservableList
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct the newProduct is added into the allProduct ObservableList
     */
    public static void addProduct(Product newProduct){
        allProduct.add(newProduct);
    }

    /**
     * @return the p which is the part being search
     */
    public static Part lookupPart(int partID) {
        ObservableList<Part> allParts = getAllParts();

        for(Part p : allParts){
            if(p.getId() == partID){
                return p;
            }
        }
        return null;
    }

    /**
     * @return the p which is the product being search
     */
    public static Product lookupProduct(int productID) {
        ObservableList<Product> allProducts = Inventory.getAllProduct();

        for(Product p : allProducts){
            if(p.getId() == productID){
                return p;
            }
        }
        return null;
    }

    /**
     * @return the partNameSearched
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partNameSearched = FXCollections.observableArrayList();
        ObservableList<Part> allParts = getAllParts();

        for(Part p : allParts){
            if(p.getName().contains(partName)){
                partNameSearched.add(p);
            }
        }
        return partNameSearched;
    }

    /**
     * @return the productNameSearched
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productNameSearched = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = getAllProduct();

        for(Product p : allProducts){
            if(p.getName().contains(productName)){
                productNameSearched.add(p);
            }
        }
        return productNameSearched;
    }

    /**
     * @param selectedPart the selectedPart is updated at the specified index on the allParts ObservableList
     */
    public static void updatePart(int index, Part selectedPart) {
        for(Part p : allParts){
            if(p.getId() == selectedPart.getId()){
                allParts.set(index, selectedPart);
            }
        }
    }

    /**
     * @param newProduct the newProduct is updated at the specified index on the allProduct ObservableList
     */
    public static void updateProduct(int index, Product newProduct) {
        for(Product p : allProduct){
            if(p.getId() == newProduct.getId()){
                allProduct.set(index, newProduct);
            }
        }
    }

    /**
     * @return true if the Part has been deleted
     */
    public static boolean deletePart(Part selectedPart) {

        for(Part p : allParts){
            if(p.getId() == selectedPart.getId()){
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if the Product has been deleted
     */
    public static boolean deleteProduct(Product selectedProduct) {

        for(Product p : allProduct){
            if(p.getId() == selectedProduct.getId()){
                allProduct.remove(p);
                return true;
            }
        }
        return false;

    }

    /**
     * @return the allParts
     */
    public static ObservableList<Part> getAllParts() { return allParts; }

    /**
     * @return the allProduct
     */
    public static ObservableList<Product> getAllProduct() {return allProduct; }
}
