package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "television")
public class ClientTelevision implements ClientService {
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
    @JoinColumn(name = "television_id")
    private List<Television> history;

    public ClientTelevision(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientTelevision() {
    }

    public ClientTelevision(int id, Date activationDate, Status status) {
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

    public List<Television> getHistory() {
        return history;
    }

    public void setHistory(List<Television> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "ClientTelevision{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", activationDate=" + activationDate +
                ", status=" + status +
                ", history=" + history +
                '}';
    }
}
