package gestiondette.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import javax.persistence.EntityManager;

import gestiondette.JpaUtil;
import gestiondette.core.enums.Role;
import gestiondette.entities.Client;
import gestiondette.entities.User;

public class AuthController {
    @FXML
    private Label roleLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private String role;

    public void setRole(String role) {
        this.role = role;
        roleLabel.setText("Connexion - " + role);

        if (role.equals("CLIENT")) {
            usernameField.setPromptText("Surname");
            passwordField.setPromptText("Telephone");
        } else {
            usernameField.setPromptText("Login");
            passwordField.setPromptText("Mot de passe");
        }
    }

    public void handleLogin() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (role.equals("CLIENT")) {
                Client client = em
                        .createQuery("SELECT c FROM Client c WHERE c.surname = :surname AND c.telephone = :telephone",
                                Client.class)
                        .setParameter("surname", usernameField.getText())
                        .setParameter("telephone", passwordField.getText())
                        .getSingleResult();
                        if (client != null) {
                            System.out.println( "--------------------------------------------------------------------------------");
                            System.out.println("Client connecté");
                            System.out.println( "--------------------------------------------------------------------------------");
                            openWindow("/views/client/index.fxml", "Menu Client");
                        }
            } else {
                User user = em.createQuery(
                        "SELECT u FROM User u WHERE u.login = :login AND u.password = :password AND u.role = :role",
                        User.class)
                        .setParameter("login", usernameField.getText())
                        .setParameter("password", passwordField.getText())
                        .setParameter("role", Role.valueOf(role))
                        .getSingleResult();

                if (role.equals("BOUTIQUIER")) {
                    System.out.println(
                            "--------------------------------------------------------------------------------");
                    System.out.println("Boutiquier connecté");
                    System.out.println(
                            "--------------------------------------------------------------------------------");
                            closeCurrentWindow();
                    openWindow("/views/boutiquier/index.fxml", "Menu Boutiquier");
                } else if (role.equals("ADMIN")) {
                    // Ajouter une vue spécifique pour Admin si nécessaire
                    System.out.println(
                            "--------------------------------------------------------------------------------");
                    System.out.println("Admin connecté");
                    System.out.println(
                            "--------------------------------------------------------------------------------");
                            //fermer la fenetre actuelle

                    closeCurrentWindow();
                    openWindow("/views/admin.fxml", "Menu Admin");
                }
            }
        } catch (Exception e) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("--------------------------------------------------------------------------------");

            showAlert("Erreur", "Identifiants invalides");
        } finally {
            em.close();
        }
    }

    private void closeCurrentWindow() {
        // Récupérer la fenêtre actuelle et la fermer
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openWindow(String fxmlPath, String title) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxmlPath))));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("--------------------------------------------------------------------------------");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
