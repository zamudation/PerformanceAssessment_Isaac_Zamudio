package main;

import classes.InHouse;
import classes.Inventory;
import classes.Outsourced;
import classes.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Isaac Zamudio
 */
public class Main extends Application {


    /**
     * This Initialize the program, including the main form
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Inventory.addPart(new InHouse(1, "Brakes", 15, 10, 1, 20, 1));
        Inventory.addPart(new InHouse(2, "Wheel", 11, 16, 1, 15, 2));
        Inventory.addPart(new Outsourced(3, "Seat", 15, 10, 1, 18, "Cool"));

        Inventory.addProduct(new Product(1000, "Giant Bike", 299.99, 5, 1, 100));
        Inventory.addProduct(new Product(1001, "Tricycle", 99.99, 3, 1, 100));


        Parent root = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        primaryStage.setTitle("Main");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
