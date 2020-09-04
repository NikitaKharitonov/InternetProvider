package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * A Phone service which belongs to one and only one
 * client. The service's current state and all its former states
 * are stored in the so-called "history".
 */
@Entity
@Table(name = "phone")
public class Phone implements Service<PhoneState> {
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
     * The date when this Phone service was created and assigned to a client.
     */
    @Column(name = "activation_date")
    private Date activationDate;

    /**
     * The current status of this Phone service which indicates
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
    @JoinColumn(name = "phone_id")
    private List<PhoneState> history;

    public Phone(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public Phone() {
    }

    public Phone(int id, Date activationDate, Status status) {
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

    public List<PhoneState> getHistory() {
        return history;
    }

    public void setHistory(List<PhoneState> history) {
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
