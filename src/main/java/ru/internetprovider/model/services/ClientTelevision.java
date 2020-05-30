package ru.internetprovider.model.services;

import java.util.Date;
import java.util.List;

public class ClientTelevision implements ClientService {

    private long id;
    private final Date activationDate;
    private ClientService.Status status;
    private List<Television> serviceList; // history


    public ClientTelevision(long id, Date activationDate, ClientService.Status status) {
        this.id = id;
        this.activationDate = activationDate;
        this.status = status;
    }

    public ClientTelevision(Date activationDate, ClientService.Status status) {
        this.activationDate = activationDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public ClientService.Status getStatus() {
        return status;
    }

    public void setStatus(ClientService.Status status) {
        this.status = status;
    }

    public List<Television> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Television> serviceList) {
        this.serviceList = serviceList;
    }

}
