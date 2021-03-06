package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * An Internet service which belongs to one and only one
 * client. The service's current state and all its former states
 * are stored in the so-called "history".
 */
@Entity
@Table(name = "internet")
public class Internet implements Service<InternetState> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The ID of the client that has this service.
     */
    @Column(name = "client_id")
    private int clientId;

    /**
     * The date when this Internet service was created and assigned to a client.
     */
    @Column(name = "activation_date")
    private Date activationDate;

    /**
     * The current status of this Internet service which indicates
     * its availability for the client: ACTIVE - the service is
     * available, SUSPENDED - the service is temporally unavailable,
     * DELETED - the service is forever unavailable.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "status")
    @Type(type = "ru.internetprovider.model.services.PostgreSQLEnumType")
    private Status status;

    /**
     * The last element of this list is the current state of the service.
     * The other elements are the
     * former states which the service once had. The elements
     * are ordered chronologically.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "internet_id")
    private List<InternetState> history;

    public Internet(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public Internet() {
    }

    public Internet(int id, Date activationDate, Status status) {
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

    public List<InternetState> getHistory() {
        return history;
    }

    public void setHistory(List<InternetState> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "ClientInternet{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", activationDate=" + activationDate +
                ", status=" + status +
                ", history=" + history +
                '}';
    }
}
