package gestiondette;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import gestiondette.core.DataInitializer;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // DataInitializer dataInitializer = new DataInitializer();
        // dataInitializer.init();
    
        Parent root = FXMLLoader.load(getClass().getResource("/views/accueil.fxml"));
        primaryStage.setTitle("Sélection du Rôle");
    
        // Définir la taille de la scène (largeur = 800, hauteur = 600 par exemple)
        Scene scene = new Scene(root, 1000, 800);
    
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}   