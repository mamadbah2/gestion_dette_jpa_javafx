package gestiondette.repositories;

import gestiondette.core.Repository;
import gestiondette.entities.DetailDebt;

public interface DetailDebtRepository extends Repository<DetailDebt> {
    DetailDebt getDetailDebtById(int idDetailDebt);
}
