package gestiondette.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccueilController {
    private void openLoginWindow(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Passer le rôle au contrôleur de connexion
            AuthController authController = loader.getController();
            authController.setRole(role);

            Stage stage = new Stage();
            stage.setTitle("Authentification - " + role);
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleBoutiquier() {
        openLoginWindow("BOUTIQUIER");
    }

    public void handleAdmin() {
        openLoginWindow("ADMIN");
    }

    public void handleClient() {
        openLoginWindow("CLIENT");
    }
}
