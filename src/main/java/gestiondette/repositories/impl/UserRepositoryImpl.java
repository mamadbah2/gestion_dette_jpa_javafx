package gestiondette.repositories.impl;

import gestiondette.entities.User;
import gestiondette.repositories.RepositoryImpl;
import gestiondette.repositories.UserRepository;

public class UserRepositoryImpl  extends RepositoryImpl<User> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public void toggleUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleUser'");
    }


    @Override
    public User selectByLogin(String login) {
       return entityManager.find(entityClass, login);
    }

    
    
}
