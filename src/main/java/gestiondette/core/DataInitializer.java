package gestiondette.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import gestiondette.entities.Article;
import gestiondette.entities.Client;
import gestiondette.entities.User;
import gestiondette.core.enums.Role;

public class DataInitializer {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestiondette");
    private EntityManager em = emf.createEntityManager();

    public void init() {
        em.getTransaction().begin();

        // Créer des utilisateurs avec différents rôles
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("adminpass");
        admin.setRole(Role.ADMIN);
        admin.setIsActive(true);

        User client = new User();
        client.setLogin("client");
        client.setPassword("clientpass");
        client.setRole(Role.CLIENT);
        client.setIsActive(true);

        User boutiquier = new User();
        boutiquier.setLogin("boutiquier");
        boutiquier.setPassword("boutiquierpass");
        boutiquier.setRole(Role.BOUTIQUIER);
        boutiquier.setIsActive(true);

        // Créer des clients associés aux utilisateurs
        Client clientEntity = new Client();
        clientEntity.setSurname("Client Surname");
        clientEntity.setTelephone("123456789");
        clientEntity.setAdresse("Client Address");
        clientEntity.setUser(client);

        //creer un article
        Article article = new Article();
        article.setLibelle("Article 1");
        article.setPrix(10000);
        article.setIs_archived(false);
        article.setQte_stock(10);
        

        em.persist(admin);
        em.persist(client);
        em.persist(boutiquier);
        em.persist(clientEntity);
        em.persist(article);

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}