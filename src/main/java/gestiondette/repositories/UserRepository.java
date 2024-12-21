package gestiondette.repositories;

import gestiondette.core.Repository;
import gestiondette.entities.User;

public interface UserRepository extends Repository<User> {
    void toggleUser(User user);
    User selectByLogin(String login);
}
