package gestiondette.repositories.impl;

import java.util.List;

import gestiondette.entities.Debt;
import gestiondette.repositories.DebtRepository;
import gestiondette.repositories.RepositoryImpl;

public class DebtRepositoryImplJpa extends RepositoryImpl<Debt> implements DebtRepository {
    public DebtRepositoryImplJpa() {
        super(Debt.class);
    }

    @Override
    public Debt getDebtById(int idDebt) {
        return entityManager.find(entityClass, idDebt);
    }

    @Override
    public List<Debt> getAllPaidDebt() {
        return entityManager.createQuery("FROM " + entityClass.getSimpleName() + " WHERE remainingAmount = 0", entityClass).getResultList();
    }

    @Override
    public List<Debt> getAllUnpaidDebt(int idClient) {
        return entityManager.createQuery("FROM " + entityClass.getSimpleName() + " WHERE remainingAmount > 0 AND client_id = " + idClient, entityClass).getResultList();
    }

    @Override
    public void archivePaidDebt(List<Debt> debts) {
        for (Debt debt : debts) {
            debt.setAchieved(true);
            entityManager.merge(debt);
        }
    }

    @Override
    public List<Debt> getDebtsFromClient(int idClient) {
        return entityManager.createQuery("FROM " + entityClass.getSimpleName() + " WHERE client_id = " + idClient, entityClass).getResultList();
    }

    @Override
    public void updateDebt(Debt debt) {
        entityManager.merge(debt);
    }

   
    
}
