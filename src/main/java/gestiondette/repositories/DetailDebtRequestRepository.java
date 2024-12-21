package gestiondette.repositories;

import gestiondette.core.Repository;
import gestiondette.entities.DetailDebtRequest;

public interface DetailDebtRequestRepository extends Repository<DetailDebtRequest> {
    DetailDebtRequest getDetailDebtById(int idDetailDebt);
} 
