package gestiondette.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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




@Entity
@Table(name = "debts")
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Double amount;
    @Temporal(TemporalType.TIMESTAMP)   
    private Date date = new Date();

    
    private Double paidAmount;

    private double remainingAmount;

    @Column(name = "is_archived", nullable = false)
    private boolean isAchieved;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL)
    private List<DetailDebt> detailDebts;
    public void setAchieved(boolean achieved) {
        this.isAchieved = achieved;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Double getPaidAmount() {
        return paidAmount;
    }
    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
    public double getRemainingAmount() {
        return remainingAmount;
    }
    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
    public boolean isAchieved() {
        return isAchieved;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public List<Payment> getPayments() {
        return payments;
    }
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
    public List<DetailDebt> getDetailDebts() {
        return detailDebts;
    }
    public void setDetailDebts(List<DetailDebt> detailDebts) {
        this.detailDebts = detailDebts;
    }
}