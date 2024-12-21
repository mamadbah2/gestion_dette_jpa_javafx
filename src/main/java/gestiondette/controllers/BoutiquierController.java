package gestiondette.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import gestiondette.entities.Article;
import gestiondette.entities.Client;
import gestiondette.entities.Debt;
import gestiondette.entities.DebtRequest;
import gestiondette.entities.DetailDebt;
import gestiondette.entities.Payment;
import gestiondette.entities.User;
import gestiondette.repositories.ClientRepository;
import gestiondette.repositories.DebtRepository;
import gestiondette.repositories.DetailDebtRepository;
import gestiondette.repositories.PaymentRepository;
import gestiondette.repositories.UserRepository;
import gestiondette.repositories.impl.ClientRepositoryImpl;
import gestiondette.repositories.impl.DebtRepositoryImplJpa;
import gestiondette.repositories.impl.DetailDebtRepositoryImpl;
import gestiondette.repositories.impl.PaymentRepositoryImpl;
import gestiondette.repositories.impl.UserRepositoryImpl;
import gestiondette.core.enums.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoutiquierController {

    @FXML
    private TextField surname;

    @FXML
    private TextField telephone;

    @FXML
    private TextField adresse;

    @FXML
    private CheckBox hasAccount;

    @FXML
    private TextField clientLogin;

    @FXML
    private PasswordField clientPassword;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TextField searchPhone;

    @FXML
    private TextField clientTelephone;

    @FXML
    private DatePicker debtDate;

    @FXML
    private TextField debtAmount;

    @FXML
    private TextField paidAmount;

    @FXML
    private TableView<Debt> clientDebtsTable;

    // @FXML
    // private DatePicker paymentDate;

    @FXML
    private TextField paymentAmount;

    @FXML
    private ComboBox<String> debtRequestStatus;

    @FXML
    private TableView<DebtRequest> debtRequestsTable;

    @FXML
    private TextField articleName;

    @FXML
    private TextField articleQuantity;

    @FXML
    private TextField articlePrice;

    @FXML
    private TableView<DetailDebt> debtArticlesTable;

    @FXML
    private TableView<Payment> debtPaymentsTable;
    @FXML
    private TableColumn<Client, String> colNom;

    @FXML
    private TableColumn<Client, String> colTelephone;

    // <DatePicker fx:id="idDette" promptText="L'id de la dette" />
    // <TextField fx:id="paymentAmount" promptText="Montant du paiement" />

    @FXML
    private TextField idDette;

    @FXML
    private TableColumn<Debt, String> dateDette;

    @FXML
    private TableColumn<Debt, Double> montantDette;

    @FXML
    private TableColumn<Debt, Double> montantVerseDette;

    @FXML
    private TableColumn<Debt, Double> montantRestantDette;

    @FXML
    private TableColumn<Debt, String> nomClientDette;

    @FXML
    private TableColumn<Client, String> colAdresse;

    @FXML
    private TableColumn<Client, Double> colCumulMontantsDus;

    @FXML
    private TableColumn<Client, Boolean> colAUnCompte;

    private List<Client> allClients;

    @FXML
    private CheckBox filterWithAccount;
    @FXML
    private TableColumn<DetailDebt, String> colLibelle;

    @FXML
    private TableColumn<DetailDebt, Integer> colQte;

    @FXML
    private TableColumn<Debt, Double> idListe;

    @FXML
    private TableColumn<DetailDebt, Double> colPrix;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestiondette");
    private EntityManager em = emf.createEntityManager();

    // -------------------------------------Repositories-------------------------------------
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;
    private final DetailDebtRepository detailDebtRepository;
    private final PaymentRepository paymentRepository;

    // ---------------------------------------------------------------------------------------
    // Initialisation des repositories
    public BoutiquierController() {
        this.em = emf.createEntityManager();
        this.clientRepository = new ClientRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.debtRepository = new DebtRepositoryImplJpa();
        this.detailDebtRepository = new DetailDebtRepositoryImpl();
        this.paymentRepository = new PaymentRepositoryImpl();
    }

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colCumulMontantsDus.setCellValueFactory(new PropertyValueFactory<>("cumulMontantsDus"));
        colAUnCompte.setCellValueFactory(new PropertyValueFactory<>("hasAccount"));
        // colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colLibelle.setCellValueFactory(cellData -> {
            DetailDebt detailDebt = cellData.getValue();
            if (detailDebt != null && detailDebt.getArticle() != null) {
                return new SimpleStringProperty(detailDebt.getArticle().getLibelle());
            }
            return new SimpleStringProperty("");
        });
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        dateDette.setCellValueFactory(new PropertyValueFactory<>("date"));
        montantDette.setCellValueFactory(new PropertyValueFactory<>("amount"));
        montantVerseDette.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        montantRestantDette.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
        idListe.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomClientDette.setCellValueFactory(cellData -> {
            Debt debt = cellData.getValue();
            if (debt != null && debt.getClient() != null) {
                return new SimpleStringProperty(debt.getClient().getSurname());
            }
            return new SimpleStringProperty("");
        });
        // debtAmount correspond a la somme des prix des articles de la dette
        // je dois parcourrir debtArticlesTable et faire la somme des prix par rapport a
        // la quantité
        debtAmount
                .setText(String.valueOf(debtArticlesTable.getItems().stream().mapToDouble(DetailDebt::getPrix).sum()));
        try {
            allClients = clientRepository.selectAll();
            // clientDebtsTable = debtRepository.selectAll();
            clientDebtsTable.getItems().setAll(debtRepository.selectAll());
            clientsTable.getItems().setAll(allClients);
        } catch (Exception e) {
            System.out.println("----------------------------------------------------------------------------------");
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des clients : " + e.getMessage());
            System.out.println("----------------------------------------------------------------------------------");
        }
        debtArticlesTable.getItems().addListener((ListChangeListener<DetailDebt>) change -> {
            double total = debtArticlesTable.getItems().stream()
                    .mapToDouble(detail -> detail.getPrix())
                    .sum();
            debtAmount.setText(String.valueOf(total));
        });
    }

    @FXML
    private void createClient() {
        try {
            // Créer un nouvel utilisateur si le client a un compte
            User user = null;
            if (hasAccount.isSelected()) {
                user = new User();
                user.setLogin(clientLogin.getText());
                user.setPassword(clientPassword.getText());
                user.setRole(Role.CLIENT);
                user.setIsActive(true);
                userRepository.insert(user);
            }

            // Créer un nouveau client
            Client client = new Client();
            client.setSurname(surname.getText());
            client.setTelephone(telephone.getText());
            client.setAdresse(adresse.getText());
            client.setUser(user);
            System.out.println("----------------------------------------------------------------------------------");
            // affichage de l'objet client
            System.out.println(client);
            System.out.println("----------------------------------------------------------------------------------");

            clientRepository.insert(client);

            // Ajouter le client à la table des clients
            clientsTable.getItems().add(client);

            // Réinitialiser les champs de saisie
            clearFieldsClient();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur et informer l'utilisateur
        }
    }

    private void clearFieldsClient() {
        surname.clear();
        telephone.clear();
        adresse.clear();
        hasAccount.setSelected(false);
        clientLogin.clear();
        clientPassword.clear();
    }

    @FXML
    private void searchClient() {
        String phone = searchPhone.getText();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.telephone = :phone",
                    Client.class);
            query.setParameter("phone", phone);
            Client client = query.getSingleResult();

            if (client != null) {
                clientsTable.getItems().setAll(client);
                searchPhone.clear();
            }
        } catch (Exception e) {
            System.out.println("Aucun client trouvé !");
        }
    }

    @FXML
    private void resetTable() {
        clientsTable.getItems().setAll(clientRepository.selectAll()); // Restaure toutes les données
    }

    @FXML
    private void createDebt() {
        em.getTransaction().begin();
        try {
            // Recherche du client
            String phone = clientTelephone.getText();
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE c.telephone = :phone",
                    Client.class);
            query.setParameter("phone", phone);
            Client client = query.getSingleResult();

            if (client != null) {
                // Création de la dette
                Debt debt = new Debt();
                debt.setDate(java.sql.Date.valueOf(debtDate.getValue()));

                // Création et calcul des détails de la dette
                List<DetailDebt> detailDebts = new ArrayList<>();
                double totalAmount = 0.0;

                for (DetailDebt item : debtArticlesTable.getItems()) {
                    DetailDebt detail = new DetailDebt();
                    detail.setDebt(debt);
                    detail.setArticle(item.getArticle());
                    detail.setQuantity(item.getQuantity());
                    detail.setPrix(item.getPrix());
                    totalAmount += detail.getPrix();
                    detailDebts.add(detail);
                }

                // Configuration de la dette avec le montant total calculé
                debt.setAmount(totalAmount);
                debt.setPaidAmount(paidAmount.getText().isEmpty() ? 0.0 : Double.parseDouble(paidAmount.getText()));
                debt.setRemainingAmount(
                        debt.getAmount() - debt.getPaidAmount() > 0 ? debt.getAmount() - debt.getPaidAmount() : 0);
                debt.setAchieved(debt.getRemainingAmount() == 0);
                debt.setClient(client);
                debt.setDetailDebts(detailDebts);

                // Persister la dette
                em.persist(debt);

                // Persister les détails
                for (DetailDebt detail : detailDebts) {
                    em.persist(detail);
                }

                // Créer le paiement si nécessaire
                if (debt.getPaidAmount() > 0) {
                    Payment payment = new Payment();
                    payment.setDate(debt.getDate());
                    payment.setAmount(debt.getPaidAmount());
                    payment.setDebt(debt);
                    em.persist(payment);
                }

                em.getTransaction().commit();

                // Mettre à jour l'interface utilisateur
                Platform.runLater(() -> {
                    try {
                        clientDebtsTable.getItems().setAll(debtRepository.selectAll());
                        clearFields();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("Erreur lors de la création de la dette : " + e.getMessage());
        }
    }

    private void clearFields() {
        clientTelephone.clear();
        debtDate.setValue(null);
        debtAmount.clear();
        paidAmount.clear();
        debtArticlesTable.getItems().clear();
    }

    @FXML
    private void recordPayment() {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            // Get debt ID and payment amount
            int debtId = Integer.parseInt(idDette.getText());
            double newPaidAmount = Double.parseDouble(paymentAmount.getText());

            // Get the debt
            Debt dette = em.find(Debt.class, debtId);
            if (dette == null) {
                throw new IllegalArgumentException("Dette non trouvée");
            }

            // Create and save payment
            Payment payment = new Payment();
            payment.setAmount(newPaidAmount);
            payment.setDate(new Date());
            payment.setDebt(dette);

            // Update debt amounts
            dette.setPaidAmount(dette.getPaidAmount() + newPaidAmount);
            dette.setRemainingAmount(
                    dette.getAmount() - dette.getPaidAmount() < 0 ? 0 : dette.getAmount() - dette.getPaidAmount());
            dette.setAchieved(dette.getRemainingAmount() <= 0);

            // Persist changes
            em.persist(payment);
            em.merge(dette);

            transaction.commit();

            // Refresh UI
            Platform.runLater(() -> {
                clientDebtsTable.getItems().setAll(debtRepository.selectAll());
                debtPaymentsTable.getItems().setAll(paymentRepository.selectAll());
                paymentAmount.clear();
                idDette.clear();
            });

            System.out.println("Paiement enregistré avec succès");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Erreur lors de l'enregistrement du paiement: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @FXML
    private void filterDebtRequests() {
        String status = debtRequestStatus.getValue();
        TypedQuery<DebtRequest> query = em.createQuery("SELECT dr FROM DebtRequest dr WHERE dr.status = :status",
                DebtRequest.class);
        query.setParameter("status", status);
        List<DebtRequest> debtRequests = query.getResultList();

        // Afficher les demandes de dettes filtrées
        debtRequestsTable.getItems().clear();
        debtRequestsTable.getItems().addAll(debtRequests);
    }

    @FXML
    private void filterClients() {
        boolean filter = filterWithAccount.isSelected();
        TypedQuery<Client> query;
        if (filter) {
            query = em.createQuery("SELECT c FROM Client c WHERE c.user IS NOT NULL", Client.class);
        } else {
            query = em.createQuery("SELECT c FROM Client c", Client.class);
        }
        List<Client> clients = query.getResultList();

        // Afficher les clients filtrés
        clientsTable.getItems().clear();
        clientsTable.getItems().addAll(clients);
    }

    @FXML
    private void addArticleToDebt() {

        String name = articleName.getText();
        TypedQuery<Article> query = em.createQuery("SELECT a FROM Article a WHERE a.libelle = :name", Article.class);
        query.setParameter("name", name);
        Article article = query.getSingleResult();

        if (article != null) {
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("Article trouvé : " + article.toString());
            System.out.println("-----------------------------------------------------------------------");
            // Créer un nouveau détail de dette
            DetailDebt detailDebt = new DetailDebt();
            detailDebt.setArticle(article);
            detailDebt.setQuantity(Integer.parseInt(articleQuantity.getText()));
            detailDebt.setPrix(article.getPrix() * detailDebt.getQuantity());

            // Ajouter le détail de dette à la table des articles de la dette
            debtArticlesTable.getItems().add(detailDebt);

            // Réinitialiser les champs de saisie
            articleName.clear();
            articleQuantity.clear();
            articlePrice.clear();

        } else {
            // Gérer le cas où aucun article n'est trouvé
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("Aucun article trouvé avec ce nom.");
            System.out.println("-----------------------------------------------------------------------");
            // em.getTransaction().rollback();
        }
    }

    @FXML
    private void viewDebtArticles() {
        Debt selectedDebt = clientDebtsTable.getSelectionModel().getSelectedItem();
        if (selectedDebt != null) {
            TypedQuery<DetailDebt> query = em.createQuery("SELECT dd FROM DetailDebt dd WHERE dd.debt.id = :debtId",
                    DetailDebt.class);
            query.setParameter("debtId", selectedDebt.getId());
            List<DetailDebt> detailDebts = query.getResultList();

            // Afficher les détails de la dette sélectionnée
            debtArticlesTable.getItems().clear();
            debtArticlesTable.getItems().addAll(detailDebts);
        } else {
            // Gérer le cas où aucune dette n'est sélectionnée
            System.out.println("Aucune dette sélectionnée.");
        }
    }

    @FXML
    private void viewDebtPayments() {
        Debt selectedDebt = clientDebtsTable.getSelectionModel().getSelectedItem();
        if (selectedDebt != null) {
            TypedQuery<Payment> query = em.createQuery("SELECT p FROM Payment p WHERE p.debt.id = :debtId",
                    Payment.class);
            query.setParameter("debtId", selectedDebt.getId());
            List<Payment> payments = query.getResultList();

            // Afficher les paiements de la dette sélectionnée
            debtPaymentsTable.getItems().clear();
            debtPaymentsTable.getItems().addAll(payments);
        } else {
            // Gérer le cas où aucune dette n'est sélectionnée
            System.out.println("Aucune dette sélectionnée.");
        }
    }

    // @FXML
    // private void validateDebtRequest() {
    //     DebtRequest selectedRequest = debtRequestsTable.getSelectionModel().getSelectedItem();
    //     if (selectedRequest != null) {
    //         em.getTransaction().begin();
    //         selectedRequest.setStatus("Validée");
    //         em.merge(selectedRequest);
    //         em.getTransaction().commit();

    //         // Mettre à jour l'affichage
    //         debtRequestsTable.refresh();
    //     } else {
    //         // Gérer le cas où aucune demande de dette n'est sélectionnée
    //         System.out.println("Aucune demande de dette sélectionnée.");
    //     }
    // }

    // @FXML
    // private void rejectDebtRequest() {
    //     DebtRequest selectedRequest = debtRequestsTable.getSelectionModel().getSelectedItem();
    //     if (selectedRequest != null) {
    //         em.getTransaction().begin();
    //         selectedRequest.setStatus("Rejetée");
    //         em.merge(selectedRequest);
    //         em.getTransaction().commit();

    //         // Mettre à jour l'affichage
    //         debtRequestsTable.refresh();
    //     } else {
    //         // Gérer le cas où aucune demande de dette n'est sélectionnée
    //         System.out.println("Aucune demande de dette sélectionnée.");
    //     }
    // }
}