package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "phone")
public class ClientPhone implements ClientService {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "client_id")
    private int clientId;
    @Column(name = "activation_date")
    private Date activationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "status")
    @Type(type = "ru.internetprovider.model.services.PostgreSQLEnumType")
    private Status status;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_id")
    private List<Phone> history;

    public ClientPhone(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientPhone() {
    }

    public ClientPhone(int id, Date activationDate, Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Phone> getHistory() {
        return history;
    }

    public void setHistory(List<Phone> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "ClientPhone{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", activationDate=" + activationDate +
                ", status=" + status +
                ", history=" + history +
                '}';
    }
}
