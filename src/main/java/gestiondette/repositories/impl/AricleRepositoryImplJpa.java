package gestiondette.repositories.impl;

import gestiondette.entities.Article;
import gestiondette.repositories.ArticleRepository;
import gestiondette.repositories.RepositoryImpl;

public class AricleRepositoryImplJpa extends RepositoryImpl<Article>  implements ArticleRepository{

    public AricleRepositoryImplJpa() {
        super(Article.class);
    }
    @Override
    public Article selectByLibelle(String libelle) {
        return entityManager.find(entityClass, libelle);
    }

}
