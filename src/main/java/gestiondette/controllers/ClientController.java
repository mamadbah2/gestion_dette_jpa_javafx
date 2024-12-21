// package gestiondette.controllers;

// import java.util.Date;
// import java.util.List;
// import java.util.Optional;

// import javax.persistence.EntityManager;
// import javax.persistence.EntityManagerFactory;
// import javax.persistence.Persistence;
// import javax.persistence.TypedQuery;

// import gestiondette.entities.Article;
// import gestiondette.entities.Client;
// import gestiondette.entities.Debt;
// import gestiondette.entities.DebtRequest;
// import gestiondette.repositories.ClientRepository;
// import gestiondette.repositories.DebtRepository;
// import gestiondette.repositories.DebtRequestRepository;
// import gestiondette.repositories.impl.ClientRepositoryImpl;
// import gestiondette.repositories.impl.DebtRepositoryImplJpa;
// import gestiondette.repositories.impl.DebtRequestRepositoryImplJpa;
// import javafx.beans.property.BooleanProperty;
// import javafx.beans.property.SimpleBooleanProperty;
// import javafx.collections.FXCollections;
// import javafx.fxml.FXML;
// import javafx.geometry.Insets;
// import javafx.scene.control.Alert;
// import javafx.scene.control.ButtonBar;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.Dialog;
// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;

// import javafx.scene.control.cell.CheckBoxTableCell;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.layout.GridPane;


// public class ClientController {

//     // Repositories
//     private final ClientRepository clientRepository;
//     private final DebtRepository debtRepository;
//     private final DebtRequestRepository debtRequestRepository;

//     // Entity Manager
//     private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestiondette");
//     private EntityManager em = emf.createEntityManager();

//     // FXML Components
//     @FXML
//     private TableView<Debt> unsolvedDebtsTable;

//     @FXML
//     private TableView<DebtRequest> debtReequestsTable;

//     // Columns for unresolved debts
//     @FXML
//     private TableColumn<Debt, Double> colMontantDette;
//     @FXML
//     private TableColumn<Debt, Double> colMontantRestant;
//     @FXML
//     private TableColumn<Debt, Double> colMontantPaye;
//     @FXML
//     private TableColumn<Debt, String> colDateDette;

//     // Columns for debt requests
//     @FXML
//     private TableColumn<DebtRequest, Double> colMontantTotalDemande;
//     @FXML
//     private TableColumn<DebtRequest, String> colDateDemande;
//     @FXML
//     private TableColumn<DebtRequest, String> colStatusDemande;

//     // Current logged in client
//     private Client currentClient;

//     // Label for displaying the total amount
//     private Label totalLabel;

//     // Constructor
//     public ClientController() {
//         this.em = emf.createEntityManager();
//         this.clientRepository = new ClientRepositoryImpl();
//         this.debtRepository = new DebtRepositoryImplJpa();
//         this.debtRequestRepository = new DebtRequestRepositoryImplJpa();
//     }

//     // Initialize method
//     @FXML
//     public void initialize() {
//         // Configuration des colonnes pour les dettes non soldées
//         colMontantDette.setCellValueFactory(new PropertyValueFactory<>("amount"));
//         colMontantRestant.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
//         colMontantPaye.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
//         colDateDette.setCellValueFactory(new PropertyValueFactory<>("date"));

//         // Configuration des colonnes pour les demandes de dettes
//         colMontantTotalDemande.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//         colDateDemande.setCellValueFactory(new PropertyValueFactory<>("date"));
//         colStatusDemande.setCellValueFactory(new PropertyValueFactory<>("status"));

//     }

//     // Méthode pour definir le client courant
//     public void setCurrentClient(@SuppressWarnings("exports") Client client) {
//         this.currentClient = client;
//         loadUnsolvedDebts();
//         loadDebtRequests();
//     }

//     // Charger les dettes non soldées
//     private void loadUnsolvedDebts() {
//         try {
//             TypedQuery<Debt> query = em.createQuery(
//                     "SELECT d FROM Debt d WHERE d.client_id = :clientId AND d.remainingamount > 0 AND d.is_achievied = false",
//                     Debt.class);
//             query.setParameter("client", currentClient);
//             List<Debt> unsolvedDebts = query.getResultList();
//             unsolvedDebtsTable.getItems().clear();
//             unsolvedDebtsTable.getItems().addAll(unsolvedDebts);
//         } catch (Exception e) {
//             System.out.println("Erreur lors du chargement des dettes non soldées : " + e.getMessage());
//         }
//     }

//     // Charger les demandes de dettes
//     private void loadDebtRequests() {
//         try {
//             TypedQuery<DebtRequest> query = em
//             .createQuery("SELECT dr FROM DebtRequest dr WHERE dr.client_id = :clientId", DebtRequest.class);
//             query.setParameter("client", currentClient);
//             List<DebtRequest> debtRequests = query.getResultList();
//             debtReequestsTable.getItems().clear();
//             debtReequestsTable.getItems().addAll(debtRequests);
//         } catch (Exception e) {
//             System.out.println("Erreur lors du chargement des demandes de dettes : " + e.getMessage());
//         }
//     }
    
//     // Méthode pour faire une nouvelle demande de dette
//     @FXML
//     private void makeDebtRequest() {
//         System.out.println("Entrez le numero de telephone du client");
//         String telephone = "123456789";

//         System.out.println("Current Client: " + (currentClient != null ? currentClient.getId() : "NULL"));
//         if (currentClient == null) {
//             showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun client sélectionné.");
//             return;
//         }
//         // Créer une boîte de dialogue personnalisée
//         Dialog<DebtRequest> dialog = new Dialog<>();
//         dialog.setTitle("Nouvelle Demande de Dette");
//         dialog.setHeaderText("Sélectionnez les articles pour la demande de dette");
    
//         // Configurer les boutons de la boîte de dialogue
//         ButtonType confirmerButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
//         dialog.getDialogPane().getButtonTypes().addAll(confirmerButtonType, ButtonType.CANCEL);
    
//         // Créer le layout principal
//         GridPane grid = new GridPane();
//         grid.setHgap(10);
//         grid.setVgap(10);
//         grid.setPadding(new Insets(20, 150, 10, 10));
    
//         // Créer le TableView pour les articles
//         TableView<Article> articleTableView = new TableView<>();
        
//         // Colonne de sélection
//         TableColumn<Article, Boolean> selectColumn = new TableColumn<>("Sélectionner");
//         selectColumn.setCellValueFactory(param -> {
//             Article article = param.getValue();
//             BooleanProperty property = new SimpleBooleanProperty(false);
            
//             property.addListener((observable, oldValue, newValue) -> {
//                 updateTotalAmount(articleTableView);
//             });
            
//             return property;
//         });
//         selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
    
//         // Colonne Libellé
//         TableColumn<Article, String> libelleColumn = new TableColumn<>("Libellé");
//         libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
    
//         // Colonne Prix
//         TableColumn<Article, Integer> prixColumn = new TableColumn<>("Prix");
//         prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
    
//         // Colonne Quantité en Stock
//         TableColumn<Article, Integer> qteColumn = new TableColumn<>("Quantité en Stock");
//         qteColumn.setCellValueFactory(new PropertyValueFactory<>("qte_stock"));
    
//         // Configurer le TableView
//         articleTableView.getColumns().setAll(FXCollections.observableArrayList(selectColumn, libelleColumn, prixColumn, qteColumn));
//         articleTableView.setEditable(true);
    
//         // Label pour afficher le total
//         totalLabel = new Label("Total: 0 €");
//         totalLabel.setStyle("-fx-font-weight: bold;");
    
//         // Charger les articles
//         List<Article> articles = loadArticles();
//         articleTableView.setItems(FXCollections.observableArrayList(articles));
    
//         // Ajouter les composants au grid
//         grid.add(articleTableView, 0, 0);
//         grid.add(totalLabel, 0, 1);
    
//         // Définir le contenu de la boîte de dialogue
//         dialog.getDialogPane().setContent(grid);
    
//         // Convertir le résultat en DebtRequest lors de la confirmation
//         dialog.setResultConverter(dialogButton -> {
//             if (dialogButton == confirmerButtonType) {
//                 // Récupérer les articles sélectionnés
//                 List<Article> selectedArticles = articleTableView.getItems().filtered(item -> 
//                     articleTableView.getSelectionModel().isSelected(articleTableView.getItems().indexOf(item))
//                 );
    
//                 if (selectedArticles.isEmpty()) {
//                     showAlert(Alert.AlertType.WARNING, "Aucun article sélectionné", "Veuillez sélectionner au moins un article.");
//                     return null;
//                 }
    
//                 DebtRequest debtRequest = new DebtRequest();
//                 debtRequest.setClient(currentClient);
//                 debtRequest.setTotalAmount(calculateTotalAmount(selectedArticles));
//                 debtRequest.setStatus("En cours");
//                 debtRequest.setDate(new Date());
                
//                 return debtRequest;
//             }
//             return null;
//         });
    
//         // Afficher la boîte de dialogue et traiter le résultat
//         Optional<DebtRequest> result = dialog.showAndWait();
    
//         result.ifPresent(debtRequest -> {
//             try {
//                 // Début de la transaction
//                 em.getTransaction().begin();
    
//                 // Persister la nouvelle demande de dette
//                 em.persist(debtRequest);
    
//                 // Valider la transaction
//                 em.getTransaction().commit();
    
//                 // Rafraîchir la liste des demandes
//                 loadDebtRequests();
    
//                 // Afficher un message de succès
//                 showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre demande de dette a été créée avec succès.");
    
//             } catch (Exception e) {
//                 // Annuler la transaction en cas d'erreur
//                 if (em.getTransaction().isActive()) {
//                     em.getTransaction().rollback();
//                 }
    
//                 // Afficher un message d'erreur
//                 showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de créer la demande de dette. Erreur : " + e.getMessage());
//             }
//         });
//     }
    
//     // Méthode pour charger les articles
//     private List<Article> loadArticles() {
//         return em.createQuery("SELECT a FROM Article a WHERE a.is_archived = false", Article.class).getResultList();
//     }
    
//     // Méthode pour calculer le total
//     private int calculateTotalAmount(List<Article> selectedArticles) {
//         return selectedArticles.stream()
//             .mapToInt(Article::getPrix)
//             .sum();
//     }
    
//     // Méthode pour mettre à jour le total
//     private void updateTotalAmount(TableView<Article> tableView) {
//         List<Article> selectedArticles = tableView.getItems().filtered(item -> 
//             tableView.getSelectionModel().isSelected(tableView.getItems().indexOf(item))
//         );
        
//         int total = calculateTotalAmount(selectedArticles);
//         totalLabel.setText(String.format("Total: %d €", total));
//     }
    
//     // Méthode utilitaire pour afficher des alertes
//     private void showAlert(Alert.AlertType type, String title, String content) {
//         Alert alert = new Alert(type);
//         alert.setTitle(title);
//         alert.setHeaderText(null);
//         alert.setContentText(content);
//         alert.showAndWait();
//     }
// }
    
package gestiondette.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import gestiondette.entities.Article;
import gestiondette.entities.Client;
import gestiondette.entities.Debt;
import gestiondette.entities.DebtRequest;
import gestiondette.repositories.ClientRepository;
import gestiondette.repositories.DebtRepository;
import gestiondette.repositories.DebtRequestRepository;
import gestiondette.repositories.impl.ClientRepositoryImpl;
import gestiondette.repositories.impl.DebtRepositoryImplJpa;
import gestiondette.repositories.impl.DebtRequestRepositoryImplJpa;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class ClientController {

    // Repositories
    private final ClientRepository clientRepository;
    private final DebtRepository debtRepository;
    private final DebtRequestRepository debtRequestRepository;

    // Entity Manager
    private final EntityManagerFactory emf;
    private final EntityManager em;

    // FXML Components
    @FXML
    private TableView<Debt> unsolvedDebtsTable;
    
    @FXML
    private TableView<DebtRequest> debtReequestsTable;

    // Columns for unresolved debts
    @FXML
    private TableColumn<Debt, Double> colMontantDette;
    @FXML
    private TableColumn<Debt, Double> colMontantRestant;
    @FXML
    private TableColumn<Debt, Double> colMontantPaye;
    @FXML
    private TableColumn<Debt, String> colDateDette;

    // Columns for debt requests
    @FXML
    private TableColumn<DebtRequest, Double> colMontantTotalDemande;
    @FXML
    private TableColumn<DebtRequest, String> colDateDemande;
    @FXML
    private TableColumn<DebtRequest, String> colStatusDemande;

    // Current logged in client
    private Client currentClient;

    // Constructor
    public ClientController() {
        this.emf = Persistence.createEntityManagerFactory("gestiondette");
        this.em = emf.createEntityManager();
        this.clientRepository = new ClientRepositoryImpl();
        this.debtRepository = new DebtRepositoryImplJpa();
        this.debtRequestRepository = new DebtRequestRepositoryImplJpa();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        // Configuration des colonnes pour les dettes non soldées
        colMontantDette.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMontantRestant.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
        colMontantPaye.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        colDateDette.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Configuration des colonnes pour les demandes de dettes
        colMontantTotalDemande.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colDateDemande.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatusDemande.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setCurrentClient(Client client) {
        this.currentClient = client;
        loadUnsolvedDebts();
        loadDebtRequests();
    }

    private void loadUnsolvedDebts() {
        try {
            TypedQuery<Debt> query = em.createQuery(
                "SELECT d FROM Debt d WHERE d.client = :client AND d.remainingAmount > 0 AND d.isAchieved = false",
                Debt.class);
            query.setParameter("client", currentClient);
            unsolvedDebtsTable.setItems(FXCollections.observableArrayList(query.getResultList()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des dettes : " + e.getMessage());
        }
    }

    private void loadDebtRequests() {
        try {
            TypedQuery<DebtRequest> query = em.createQuery(
                "SELECT dr FROM DebtRequest dr WHERE dr.client = :client",
                DebtRequest.class);
            query.setParameter("client", currentClient);
            debtReequestsTable.setItems(FXCollections.observableArrayList(query.getResultList()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des demandes : " + e.getMessage());
        }
    }

    @FXML
    private void makeDebtRequest() {
        Optional<String> phoneNumber = showPhoneInputDialog();
        if (phoneNumber.isPresent()) {
            findClientAndShowDebtRequest(phoneNumber.get());
        }
    }

    private Optional<String> showPhoneInputDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Recherche Client");
        dialog.setHeaderText("Entrez le numéro de téléphone du client");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField phoneField = new TextField();
        phoneField.setPromptText("Numéro de téléphone");
        grid.add(new Label("Téléphone:"), 0, 0);
        grid.add(phoneField, 1, 0);

        ButtonType confirmButtonType = new ButtonType("Rechercher", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return phoneField.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void findClientAndShowDebtRequest(String phoneNumber) {
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.telephone = :telephone",
                Client.class);
            query.setParameter("telephone", phoneNumber);
            
            currentClient = query.getSingleResult();
            showDebtRequestDialog(currentClient);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                "Aucun client trouvé avec ce numéro : " + phoneNumber);
        }
    }

    private void showDebtRequestDialog(Client client) {
        Dialog<DebtRequest> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Demande de Dette");
        dialog.setHeaderText("Sélectionnez les articles pour la demande de dette");

        ButtonType confirmButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Création du TableView pour les articles
        TableView<Article> articleTableView = createArticleTableView();
        
        // Label pour le montant total
        Label totalLabel = new Label("Total: 0 €");
        totalLabel.setStyle("-fx-font-weight: bold");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(articleTableView, 0, 0);
        grid.add(totalLabel, 0, 1);

        dialog.getDialogPane().setContent(grid);

        // Chargement des articles
        loadArticlesIntoTable(articleTableView);

        // Gestion de la sélection et mise à jour du total
        articleTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> updateTotalAmount(articleTableView, totalLabel));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return createDebtRequest(client, articleTableView);
            }
            return null;
        });

        Optional<DebtRequest> result = dialog.showAndWait();
        result.ifPresent(this::saveDebtRequest);
    }

    private TableView<Article> createArticleTableView() {
        TableView<Article> tableView = new TableView<>();
        
        TableColumn<Article, Boolean> selectCol = new TableColumn<>("Sélectionner");
        selectCol.setCellValueFactory(param -> new SimpleBooleanProperty(false));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        TableColumn<Article, String> libelleCol = new TableColumn<>("Libellé");
        libelleCol.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        TableColumn<Article, Integer> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        TableColumn<Article, Integer> stockCol = new TableColumn<>("Quantité en Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("qte_stock"));

        tableView.getColumns().addAll(selectCol, libelleCol, prixCol, stockCol);
        tableView.setEditable(true);

        return tableView;
    }

    private void loadArticlesIntoTable(TableView<Article> tableView) {
        try {
            List<Article> articles = em.createQuery(
                "SELECT a FROM Article a WHERE a.is_archived = false", 
                Article.class).getResultList();
            tableView.setItems(FXCollections.observableArrayList(articles));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                "Erreur lors du chargement des articles : " + e.getMessage());
        }
    }

    private void updateTotalAmount(TableView<Article> tableView, Label totalLabel) {
        double total = tableView.getItems().stream()
            .filter(item -> tableView.getSelectionModel().getSelectedItems().contains(item))
            .mapToDouble(Article::getPrix)
            .sum();
        totalLabel.setText(String.format("Total: %.2f €", total));
    }

    private DebtRequest createDebtRequest(Client client, TableView<Article> tableView) {
        List<Article> selectedArticles = tableView.getSelectionModel().getSelectedItems();
        
        if (selectedArticles.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                "Attention", "Veuillez sélectionner au moins un article.");
            return null;
        }

        DebtRequest debtRequest = new DebtRequest();
        debtRequest.setClient(client);
        debtRequest.setTotalAmount(calculateTotalAmount(selectedArticles));
        debtRequest.setStatus("En cours");
        debtRequest.setDate(new Date());
        
        return debtRequest;
    }

    private double calculateTotalAmount(List<Article> articles) {
        return articles.stream()
            .mapToDouble(Article::getPrix)
            .sum();
    }

    private void saveDebtRequest(DebtRequest debtRequest) {
        try {
            em.getTransaction().begin();
            em.persist(debtRequest);
            em.getTransaction().commit();
            
            loadDebtRequests(); // Rafraîchir la liste
            showAlert(Alert.AlertType.INFORMATION,
                "Succès", "Demande de dette créée avec succès.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Erreur",
                "Erreur lors de la création de la demande : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}