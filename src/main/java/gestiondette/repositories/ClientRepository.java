package gestiondette.repositories;

import gestiondette.core.Repository;
import gestiondette.entities.Client;

public interface ClientRepository extends Repository<Client> {
    Client selectByTel(String tel);
    Client selectBySurname(String surname);
    Client selectById(int id);
    void   createAccount(Client client);
}