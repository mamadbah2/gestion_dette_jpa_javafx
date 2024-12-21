package gestiondette.repositories.impl;

import gestiondette.entities.DetailDebt;
import gestiondette.repositories.DetailDebtRepository;
import gestiondette.repositories.RepositoryImpl;

public class DetailDebtRepositoryImpl extends RepositoryImpl<DetailDebt> implements DetailDebtRepository {
    public DetailDebtRepositoryImpl() {
        super(DetailDebt.class);
    }

    @Override
    public DetailDebt getDetailDebtById(int idDetailDebt) {
       return entityManager.find(entityClass, idDetailDebt);
    }

   
    
}
