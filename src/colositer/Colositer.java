/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author pavel
 */
public class Colositer extends Application {

    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Colositer.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("Colositer.fxml"));

        Parent root = (Parent) loader.load();
        ColositerController controller = (ColositerController) loader.getController();
        controller.setStage(primaryStage); // or what you want to do
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        

    }
}