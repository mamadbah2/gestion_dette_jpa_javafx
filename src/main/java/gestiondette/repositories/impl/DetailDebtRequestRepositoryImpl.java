package gestiondette.repositories.impl;

import gestiondette.entities.DetailDebtRequest;
import gestiondette.repositories.DetailDebtRequestRepository;
import gestiondette.repositories.RepositoryImpl;

public class DetailDebtRequestRepositoryImpl extends RepositoryImpl<DetailDebtRequest> implements DetailDebtRequestRepository {
    public DetailDebtRequestRepositoryImpl() {
        super(DetailDebtRequest.class);
    }

    @Override
    public DetailDebtRequest getDetailDebtById(int idDetailDebt) {
       return entityManager.find(entityClass, idDetailDebt);
    }
    
}
