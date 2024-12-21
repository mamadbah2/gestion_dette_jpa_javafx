package gestiondette.repositories;

import gestiondette.core.Repository;
import gestiondette.entities.Article;

public interface ArticleRepository  extends Repository<Article>{
    Article selectByLibelle(String libelle);
    Article selectById(int id);

}
