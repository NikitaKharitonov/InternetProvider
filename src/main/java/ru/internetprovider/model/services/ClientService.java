package ru.internetprovider.model.services;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public class ClientService<T extends Service> {

    public enum Status {
        SUSPENDED, ACTIVE, DISCONNECTED
    }

    private long id;
    private final Date activationDate;
    private Status status;
    private List<T> serviceList; // history


    public ClientService(long id, Date activationDate, Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientService(Date activationDate, Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<T> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<T> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public String toString() {
        return "ClientService{" +
                "id=" + id +
                ", activationDate=" + activationDate +
                ", status=" + status +
                ", serviceList=" + serviceList +
                '}';
    }
}
