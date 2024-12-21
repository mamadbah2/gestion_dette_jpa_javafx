package gestiondette.repositories.impl;

import gestiondette.entities.DebtRequest;
import gestiondette.repositories.DebtRequestRepository;
import gestiondette.repositories.RepositoryImpl;

public class DebtRequestRepositoryImplJpa extends RepositoryImpl<DebtRequest> implements DebtRequestRepository {

    public DebtRequestRepositoryImplJpa() {
        super(DebtRequest.class);
    }
    @Override
    public DebtRequest getDebtRequestByClient(int idClient) {
        return entityManager.createQuery("FROM " + entityClass.getSimpleName() + " WHERE client_id = " + idClient, entityClass).getSingleResult();
    }

    @Override
    public void updateStatus(DebtRequest debtRequest) {
        entityManager.merge(debtRequest);
    }

    @Override
    public DebtRequest getDebtRequestById(int idDebtRequest) {
        return entityManager.find(entityClass, idDebtRequest);
    }

    
}
