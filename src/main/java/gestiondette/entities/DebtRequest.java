package gestiondette.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Entity
@Table(name = "debtRequests")
public class DebtRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Temporal(TemporalType.TIMESTAMP)
    Date date;
    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;
    private double totalAmount;
    @OneToMany(mappedBy = "debtRequest", cascade = CascadeType.ALL)
    private List<DetailDebtRequest> detailDebts = new ArrayList<DetailDebtRequest>();
    String status;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<DetailDebtRequest> getDetailDebts() {
        return detailDebts;
    }
    public void setDetailDebts(List<DetailDebtRequest> detailDebts) {
        this.detailDebts = detailDebts;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
