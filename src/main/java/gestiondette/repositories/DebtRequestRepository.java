package gestiondette.repositories;

import gestiondette.entities.DebtRequest;
import gestiondette.core.Repository;

public interface DebtRequestRepository extends Repository<DebtRequest> {
    DebtRequest getDebtRequestByClient(int idClient);
    void updateStatus(DebtRequest debtRequest);
    DebtRequest getDebtRequestById(int idDebtRequest);
}
