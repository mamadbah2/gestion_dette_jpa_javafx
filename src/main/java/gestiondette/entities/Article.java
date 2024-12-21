package gestiondette.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 50, unique = true)
    private String libelle;

    private int qte_stock;
    @Column(columnDefinition = "boolean default false")
    private boolean is_archived;
    private int prix;

    @OneToMany(mappedBy = "article")
    private List<DetailDebtRequest> detailDebts;


    @Override
    public String toString() {
        return "Article [id=" + id + ", libelle=" + libelle + ", qteStock=" + qte_stock + ", prix=" + prix + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQte_stock() {
        return qte_stock;
    }

    public void setQte_stock(int qte_stock) {
        this.qte_stock = qte_stock;
    }

    public boolean isIs_archived() {
        return is_archived;
    }

    public void setIs_archived(boolean is_archived) {
        this.is_archived = is_archived;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public List<DetailDebtRequest> getDetailDebts() {
        return detailDebts;
    }

    public void setDetailDebts(List<DetailDebtRequest> detailDebts) {
        this.detailDebts = detailDebts;
    }
}