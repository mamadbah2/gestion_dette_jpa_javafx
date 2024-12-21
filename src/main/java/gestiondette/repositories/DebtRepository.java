package gestiondette.repositories;

import java.util.List;

import gestiondette.entities.Debt;
import gestiondette.core.Repository;

public interface DebtRepository extends Repository<Debt> {
    public List<Debt> getAllPaidDebt();
    public List<Debt> getAllUnpaidDebt(int idClient);
    public void archivePaidDebt(List<Debt> debts);
    public List<Debt> getDebtsFromClient( int idClient);
    public Debt getDebtById(int id);
    public void updateDebt(Debt debt);
}