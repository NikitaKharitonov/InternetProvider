package ru.internetprovider.model.services;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * A Television service which belongs to one and only one
 * client. The service's current state and all its former states
 * are stored in the so-called "history".
 */
@Entity
@Table(name = "television")
public class Television implements Service<TelevisionState> {
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
     * The date when this Television service was created and assigned to a client.
     */
    @Column(name = "activation_date")
    private Date activationDate;

    /**
     * The current status of this Television service which indicates
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
    @JoinColumn(name = "television_id")
    private List<TelevisionState> history;

    public Television(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public Television() {
    }

    public Television(int id, Date activationDate, Status status) {
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

    public List<TelevisionState> getHistory() {
        return history;
    }

    public void setHistory(List<TelevisionState> history) {
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
